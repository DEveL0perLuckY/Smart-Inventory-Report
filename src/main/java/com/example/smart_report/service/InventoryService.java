package com.example.smart_report.service;

import com.example.smart_report.domain.HealthTest;
import com.example.smart_report.domain.Inventory;
import com.example.smart_report.model.InventoryDTO;
import com.example.smart_report.repos.HealthTestRepository;
import com.example.smart_report.repos.InventoryRepository;
import com.example.smart_report.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@SuppressWarnings("null")

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final HealthTestRepository healthTestRepository;

    public InventoryService(final InventoryRepository inventoryRepository,
            final HealthTestRepository healthTestRepository) {
        this.inventoryRepository = inventoryRepository;
        this.healthTestRepository = healthTestRepository;
    }

    public List<InventoryDTO> findAll() {
        final List<Inventory> inventories = inventoryRepository.findAll(Sort.by("inventoryId"));
        return inventories.stream()
                .map(inventory -> mapToDTO(inventory, new InventoryDTO()))
                .toList();
    }

    public InventoryDTO get(final Integer inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .map(inventory -> mapToDTO(inventory, new InventoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final InventoryDTO inventoryDTO) {
        final Inventory inventory = new Inventory();
        mapToEntity(inventoryDTO, inventory);
        return inventoryRepository.save(inventory).getInventoryId();
    }

    public void update(final Integer inventoryId, final InventoryDTO inventoryDTO) {
        final Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(inventoryDTO, inventory);
        inventoryRepository.save(inventory);
    }

    public void delete(final Integer inventoryId) {
        inventoryRepository.deleteById(inventoryId);
    }

    private InventoryDTO mapToDTO(final Inventory inventory, final InventoryDTO inventoryDTO) {
        inventoryDTO.setInventoryId(inventory.getInventoryId());
        inventoryDTO.setQuantity(inventory.getQuantity());
        inventoryDTO.setLastUpdated(inventory.getLastUpdated());
        inventoryDTO.setItem(inventory.getItem() == null ? null : inventory.getItem().getTestId());
        return inventoryDTO;
    }

    private Inventory mapToEntity(final InventoryDTO inventoryDTO, final Inventory inventory) {
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setLastUpdated(inventoryDTO.getLastUpdated());
        final HealthTest item = inventoryDTO.getItem() == null ? null
                : healthTestRepository.findById(inventoryDTO.getItem())
                        .orElseThrow(() -> new NotFoundException("item not found"));
        inventory.setItem(item);
        return inventory;
    }

}
