package com.example.smart_report.service;

import com.example.smart_report.domain.HealthParameter;
import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.domain.Inventory;
import com.example.smart_report.domain.Machine;
import com.example.smart_report.domain.Reagent;
import com.example.smart_report.domain.TestLabMapping;
import com.example.smart_report.model.HealthTestDTO;
import com.example.smart_report.repos.HealthParameterRepository;
import com.example.smart_report.repos.HealthTestRepository;
import com.example.smart_report.repos.InventoryRepository;
import com.example.smart_report.repos.MachineRepository;
import com.example.smart_report.repos.ReagentRepository;
import com.example.smart_report.repos.TestLabMappingRepository;
import com.example.smart_report.util.NotFoundException;
import com.example.smart_report.util.ReferencedWarning;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@SuppressWarnings("null")

@Service
public class HealthTestService {

    private final HealthTestRepository healthTestRepository;
    private final MachineRepository machineRepository;
    private final ReagentRepository reagentRepository;
    private final HealthParameterRepository healthParameterRepository;
    private final TestLabMappingRepository testLabMappingRepository;
    private final InventoryRepository inventoryRepository;

    public HealthTestService(final HealthTestRepository healthTestRepository,
            final MachineRepository machineRepository, final ReagentRepository reagentRepository,
            final HealthParameterRepository healthParameterRepository,
            final TestLabMappingRepository testLabMappingRepository,
            final InventoryRepository inventoryRepository) {
        this.healthTestRepository = healthTestRepository;
        this.machineRepository = machineRepository;
        this.reagentRepository = reagentRepository;
        this.healthParameterRepository = healthParameterRepository;
        this.testLabMappingRepository = testLabMappingRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public List<HealthTestDTO> findAll() {
        final List<HealthTest> healthTests = healthTestRepository.findAll(Sort.by("testId"));
        return healthTests.stream()
                .map(healthTest -> mapToDTO(healthTest, new HealthTestDTO()))
                .toList();
    }

    public HealthTestDTO get(final Integer testId) {
        return healthTestRepository.findById(testId)
                .map(healthTest -> mapToDTO(healthTest, new HealthTestDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final HealthTestDTO healthTestDTO) {
        final HealthTest healthTest = new HealthTest();
        mapToEntity(healthTestDTO, healthTest);
        return healthTestRepository.save(healthTest).getTestId();
    }

    public void update(final Integer testId, final HealthTestDTO healthTestDTO) {
        final HealthTest healthTest = healthTestRepository.findById(testId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(healthTestDTO, healthTest);
        healthTestRepository.save(healthTest);
    }

    public void delete(final Integer testId) {
        final HealthTest healthTest = healthTestRepository.findById(testId)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        reagentRepository.findAllByReagentTestMappingHealthTests(healthTest)
                .forEach(reagent -> reagent.getReagentTestMappingHealthTests().remove(healthTest));
        healthTestRepository.delete(healthTest);
    }

    private HealthTestDTO mapToDTO(final HealthTest healthTest, final HealthTestDTO healthTestDTO) {
        healthTestDTO.setTestId(healthTest.getTestId());
        healthTestDTO.setTestName(healthTest.getTestName());
        healthTestDTO.setTestType(healthTest.getTestType());
        healthTestDTO.setTestMachineMappingMachines(healthTest.getTestMachineMappingMachines().stream()
                .map(machine -> machine.getMachineId())
                .toList());
        return healthTestDTO;
    }

    private HealthTest mapToEntity(final HealthTestDTO healthTestDTO, final HealthTest healthTest) {
        healthTest.setTestName(healthTestDTO.getTestName());
        healthTest.setTestType(healthTestDTO.getTestType());
        final List<Machine> testMachineMappingMachines = iterableToList(machineRepository.findAllById(
                healthTestDTO.getTestMachineMappingMachines() == null ? Collections.emptyList()
                        : healthTestDTO.getTestMachineMappingMachines()));
        if (testMachineMappingMachines.size() != (healthTestDTO.getTestMachineMappingMachines() == null ? 0
                : healthTestDTO.getTestMachineMappingMachines().size())) {
            throw new NotFoundException("one of testMachineMappingMachines not found");
        }
        healthTest.setTestMachineMappingMachines(new HashSet<>(testMachineMappingMachines));
        return healthTest;
    }

    private <T> List<T> iterableToList(final Iterable<T> iterable) {
        final List<T> list = new ArrayList<T>();
        iterable.forEach(item -> list.add(item));
        return list;
    }

    public ReferencedWarning getReferencedWarning(final Integer testId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final HealthTest healthTest = healthTestRepository.findById(testId)
                .orElseThrow(NotFoundException::new);
        final Machine testMachine = machineRepository.findFirstByTest(healthTest);
        if (testMachine != null) {
            referencedWarning.setKey("healthTest.machine.test.referenced");
            referencedWarning.addParam(testMachine.getMachineId());
            return referencedWarning;
        }
        final HealthParameter testHealthParameter = healthParameterRepository.findFirstByTest(healthTest);
        if (testHealthParameter != null) {
            referencedWarning.setKey("healthTest.healthParameter.test.referenced");
            referencedWarning.addParam(testHealthParameter.getParameterId());
            return referencedWarning;
        }
        final TestLabMapping testTestLabMapping = testLabMappingRepository.findFirstByTest(healthTest);
        if (testTestLabMapping != null) {
            referencedWarning.setKey("healthTest.testLabMapping.test.referenced");
            referencedWarning.addParam(testTestLabMapping.getLabId());
            return referencedWarning;
        }
        final Inventory itemInventory = inventoryRepository.findFirstByItem(healthTest);
        if (itemInventory != null) {
            referencedWarning.setKey("healthTest.inventory.item.referenced");
            referencedWarning.addParam(itemInventory.getInventoryId());
            return referencedWarning;
        }
        return null;
    }

    public byte[] generatePdfForHealthTest(HealthTest healthTest) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        try {
            Font headingFont = new Font(Font.HELVETICA, 14, Font.BOLD, Color.BLUE);
            Font subheadingFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

            addHealthTestToPdf(document, healthTest, headingFont, subheadingFont, normalFont);

        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }

    private void addHealthTestToPdf(Document document, HealthTest healthTest, Font headingFont, Font subheadingFont,
            Font normalFont) throws DocumentException {
        Paragraph title = new Paragraph("Health Test Report", headingFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(Chunk.NEWLINE);

        Paragraph testInfo = new Paragraph();
        testInfo.add(new Chunk("Health Test ID: ", subheadingFont));
        testInfo.add(new Chunk(String.valueOf(healthTest.getTestId()), normalFont));
        testInfo.add(Chunk.NEWLINE);
        testInfo.add(new Chunk("Test Name: ", subheadingFont));
        testInfo.add(new Chunk(healthTest.getTestName(), normalFont));
        testInfo.add(Chunk.NEWLINE);
        testInfo.add(new Chunk("Test Type: ", subheadingFont));
        testInfo.add(new Chunk(healthTest.getTestType(), normalFont));
        document.add(testInfo);

        document.add(Chunk.NEWLINE);

        // Add related entities information
        addMachinesToPdf(document, healthTest.getTestMachines(), normalFont);
        addHealthParametersToPdf(document, healthTest.getTestHealthParameters(), normalFont);
        addTestLabMappingsToPdf(document, healthTest.getTestTestLabMappings(), normalFont);
        addReagentsToPdf(document, healthTest.getReagentTestMappingReagents(), normalFont);
        addInventoriesToPdf(document, healthTest.getItemInventories(), normalFont);
        addTestMachineMappingMachinesToPdf(document, healthTest.getTestMachineMappingMachines(), normalFont);

        document.add(Chunk.NEWLINE);
        document.add(new LineSeparator());

    }

    private void addMachinesToPdf(Document document, Set<Machine> machines, Font normalFont) throws DocumentException {
        if (machines != null && !machines.isEmpty()) {
            Paragraph machinesParagraph = new Paragraph("Machines:", normalFont);
            document.add(machinesParagraph);

            for (Machine machine : machines) {
                Paragraph machineInfo = new Paragraph();
                machineInfo.add(new Chunk("Machine ID: ", normalFont));
                machineInfo.add(new Chunk(String.valueOf(machine.getMachineId()), normalFont));
                machineInfo.add(Chunk.NEWLINE);
                machineInfo.add(new Chunk("Machine Name: ", normalFont));
                machineInfo.add(new Chunk(machine.getMachineName(), normalFont));
                document.add(machineInfo);
                document.add(Chunk.NEWLINE);
            }
        }
    }

    private void addHealthParametersToPdf(Document document, Set<HealthParameter> healthParameters, Font normalFont)
            throws DocumentException {
        if (healthParameters != null && !healthParameters.isEmpty()) {
            Paragraph healthParametersParagraph = new Paragraph("Health Parameters:", normalFont);
            document.add(healthParametersParagraph);

            for (HealthParameter parameter : healthParameters) {
                Paragraph parameterInfo = new Paragraph();
                parameterInfo.add(new Chunk("Parameter ID: ", normalFont));
                parameterInfo.add(new Chunk(String.valueOf(parameter.getParameterId()), normalFont));
                parameterInfo.add(Chunk.NEWLINE);
                parameterInfo.add(new Chunk("Parameter Name: ", normalFont));
                parameterInfo.add(new Chunk(parameter.getParameterName(), normalFont));
                document.add(parameterInfo);
                document.add(Chunk.NEWLINE);
            }
        }
    }

    private void addTestLabMappingsToPdf(Document document, Set<TestLabMapping> testLabMappings, Font normalFont)
            throws DocumentException {
        if (testLabMappings != null && !testLabMappings.isEmpty()) {
            Paragraph testLabMappingsParagraph = new Paragraph("Test Lab Mappings:", normalFont);
            document.add(testLabMappingsParagraph);

            for (TestLabMapping mapping : testLabMappings) {
                Paragraph mappingInfo = new Paragraph();
                mappingInfo.add(new Chunk("Lab ID: ", normalFont));
                mappingInfo.add(new Chunk(String.valueOf(mapping.getLabId()), normalFont));
                document.add(mappingInfo);
                document.add(Chunk.NEWLINE);
            }
        }
    }

    private void addReagentsToPdf(Document document, Set<Reagent> reagents, Font normalFont) throws DocumentException {
        if (reagents != null && !reagents.isEmpty()) {
            Paragraph reagentsParagraph = new Paragraph("Reagents:", normalFont);
            document.add(reagentsParagraph);

            for (Reagent reagent : reagents) {
                Paragraph reagentInfo = new Paragraph();
                reagentInfo.add(new Chunk("Reagent ID: ", normalFont));
                reagentInfo.add(new Chunk(String.valueOf(reagent.getReagentId()), normalFont));
                reagentInfo.add(Chunk.NEWLINE);
                reagentInfo.add(new Chunk("Reagent Name: ", normalFont));
                reagentInfo.add(new Chunk(reagent.getReagentName(), normalFont));
                document.add(reagentInfo);
                document.add(Chunk.NEWLINE);
            }
        }
    }

    private void addInventoriesToPdf(Document document, Set<Inventory> inventories, Font normalFont)
            throws DocumentException {
        if (inventories != null && !inventories.isEmpty()) {
            Paragraph inventoriesParagraph = new Paragraph("Inventories:", normalFont);
            document.add(inventoriesParagraph);

            for (Inventory inventory : inventories) {
                Paragraph inventoryInfo = new Paragraph();
                inventoryInfo.add(new Chunk("Inventory ID: ", normalFont));
                inventoryInfo.add(new Chunk(String.valueOf(inventory.getInventoryId()), normalFont));
                inventoryInfo.add(Chunk.NEWLINE);
                inventoryInfo.add(new Chunk("Quantity: ", normalFont));
                inventoryInfo.add(new Chunk(String.valueOf(inventory.getQuantity()), normalFont));
                document.add(inventoryInfo);
                document.add(Chunk.NEWLINE);
            }
        }
    }

    private void addTestMachineMappingMachinesToPdf(Document document, Set<Machine> testMachineMappingMachines,
            Font normalFont) throws DocumentException {
        if (testMachineMappingMachines != null && !testMachineMappingMachines.isEmpty()) {
            document.add(new Paragraph("Test Machine Mapping Machines:", normalFont));
            for (Machine machine : testMachineMappingMachines) {
                document.add(new Paragraph("Machine ID: " + machine.getMachineId(), normalFont));
                document.add(new Paragraph("Machine Name: " + machine.getMachineName(), normalFont));
                document.add(Chunk.NEWLINE);
            }
        }
    }

}
