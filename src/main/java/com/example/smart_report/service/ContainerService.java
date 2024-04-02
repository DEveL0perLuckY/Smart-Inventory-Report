package com.example.smart_report.service;

import com.example.smart_report.domain.Container;
import com.example.smart_report.model.ContainerDTO;
import com.example.smart_report.repos.ContainerRepository;
import com.example.smart_report.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@SuppressWarnings("null")
@Service
public class ContainerService {

    private final ContainerRepository containerRepository;

    public ContainerService(final ContainerRepository containerRepository) {
        this.containerRepository = containerRepository;
    }

    public List<ContainerDTO> findAll() {
        final List<Container> containers = containerRepository.findAll(Sort.by("containerId"));
        return containers.stream()
                .map(container -> mapToDTO(container, new ContainerDTO()))
                .toList();
    }

    public ContainerDTO get(final Integer containerId) {
        return containerRepository.findById(containerId)
                .map(container -> mapToDTO(container, new ContainerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ContainerDTO containerDTO) {
        final Container container = new Container();
        mapToEntity(containerDTO, container);
        return containerRepository.save(container).getContainerId();
    }

    public void update(final Integer containerId, final ContainerDTO containerDTO) {
        final Container container = containerRepository.findById(containerId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(containerDTO, container);
        containerRepository.save(container);
    }

    public void delete(final Integer containerId) {
        containerRepository.deleteById(containerId);
    }

    private ContainerDTO mapToDTO(final Container container, final ContainerDTO containerDTO) {
        containerDTO.setContainerId(container.getContainerId());
        containerDTO.setContainerName(container.getContainerName());
        containerDTO.setChemicals(container.getChemicals());
        containerDTO.setTransportMethod(container.getTransportMethod());
        return containerDTO;
    }

    private Container mapToEntity(final ContainerDTO containerDTO, final Container container) {
        container.setContainerName(containerDTO.getContainerName());
        container.setChemicals(containerDTO.getChemicals());
        container.setTransportMethod(containerDTO.getTransportMethod());
        return container;
    }

}
