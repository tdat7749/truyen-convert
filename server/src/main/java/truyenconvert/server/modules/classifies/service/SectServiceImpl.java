package truyenconvert.server.modules.classifies.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Sect;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.classifies.dto.CreateSectDTO;
import truyenconvert.server.modules.classifies.dto.EditSectDTO;
import truyenconvert.server.modules.classifies.exceptions.WorldContextNotFoundException;
import truyenconvert.server.modules.classifies.repository.SectRepository;
import truyenconvert.server.modules.classifies.vm.SectVm;
import truyenconvert.server.modules.common.service.MappingService;
import truyenconvert.server.modules.common.service.MessageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SectServiceImpl implements SectService{

    private final String CACHE_VALUE = "sects";

    private static final Logger LOGGER = LoggerFactory.getLogger(SectServiceImpl.class);

    private final SectRepository sectRepository;
    private final MappingService mappingService;
    private final MessageService messageService;

    public SectServiceImpl(
            SectRepository sectRepository,
            MappingService mappingService,
            MessageService messageService
    ){
        this.sectRepository = sectRepository;
        this.mappingService = mappingService;
        this.messageService = messageService;
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<SectVm> createSect(CreateSectDTO dto, User user) {
        Sect sect = Sect.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        var result = mappingService.getSectVm(sect);

        LOGGER.info(messageService.getMessage("sect.log.created.success"),user.getId(),result.getId());
        return new ResponseSuccess<>(messageService.getMessage("sect.created.success"),result);
    }

    @Override
    @Cacheable(value = CACHE_VALUE)
    public ResponseSuccess<List<SectVm>> getAllSect() {
        var rawResult = sectRepository.findAll(Sort.by(Sort.Direction.ASC,"id"));

        var result = rawResult.stream().map(mappingService::getSectVm).toList();

        return new ResponseSuccess<>("Thành công.",result);
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<SectVm> editSect(EditSectDTO dto, int id, User user) {
        var sectFound = sectRepository.findById(id).orElse(null);
        if(sectFound == null){

            LOGGER.error(messageService.getMessage("sect.log.not-found"));
            throw new WorldContextNotFoundException(messageService.getMessage("sect.not-found"));
        }

        sectFound.setDescription(dto.getDescription());
        sectFound.setTitle(dto.getTitle());
        sectFound.setUpdatedAt(LocalDateTime.now());

        var save = sectRepository.save(sectFound);

        var result = mappingService.getSectVm(save);

        LOGGER.info(messageService.getMessage("sect.log.edit.success"),user.getId(),result.getId());
        return new ResponseSuccess<>(messageService.getMessage("sect.edit.success"),result);
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<Boolean> deleteSect(int id, User user) {
        return null;
    }

    @Override
    public Optional<Sect> findById(int id) {
        return sectRepository.findById(id);
    }

    @Override
    @Cacheable(value = CACHE_VALUE, key = "#id")
    public ResponseSuccess<SectVm> getById(int id) {
        var sectFound = sectRepository.findById(id).orElse(null);
        if(sectFound == null){

            LOGGER.error(messageService.getMessage("sect.log.not-found"));
            throw new WorldContextNotFoundException(messageService.getMessage("sect.not-found"));
        }

        var result = mappingService.getSectVm(sectFound);

        return new ResponseSuccess<>("Thành công.",result);
    }
}
