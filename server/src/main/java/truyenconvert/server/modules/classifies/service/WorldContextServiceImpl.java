package truyenconvert.server.modules.classifies.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.models.WorldContext;
import truyenconvert.server.modules.classifies.dto.CreateWorldContextDTO;
import truyenconvert.server.modules.classifies.dto.EditWorldContextDTO;
import truyenconvert.server.modules.classifies.exceptions.WorldContextNotFoundException;
import truyenconvert.server.modules.classifies.repository.WorldContextRepository;
import truyenconvert.server.modules.classifies.vm.WorldContextVm;
import truyenconvert.server.modules.common.service.MappingService;
import truyenconvert.server.modules.common.service.MessageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WorldContextServiceImpl implements WorldContextService{

    private final String CACHE_VALUE = "world_contexts";

    private static final Logger LOGGER = LoggerFactory.getLogger(WorldContextServiceImpl.class);

    private final WorldContextRepository worldContextRepository;
    private final MappingService mappingService;
    private final MessageService messageService;

    public WorldContextServiceImpl(
            WorldContextRepository worldContextRepository,
            MappingService mappingService,
            MessageService messageService
    ){
        this.worldContextRepository = worldContextRepository;
        this.mappingService = mappingService;
        this.messageService = messageService;
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<WorldContextVm> createWorldContext(CreateWorldContextDTO dto, User user) {
        WorldContext worldContext = WorldContext.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        var result = mappingService.getWorldContextVm(worldContext);

        LOGGER.info(messageService.getMessage("world-context.log.created.success"),user.getId(),result.getId());
        return new ResponseSuccess<>(messageService.getMessage("world-context.created.success"),result);
    }

    @Override
    @Cacheable(value = CACHE_VALUE)
    public ResponseSuccess<List<WorldContextVm>> getAllWorldContext() {
        var rawResult = worldContextRepository.findAll(Sort.by(Sort.Direction.ASC,"id"));

        var result = rawResult.stream().map(mappingService::getWorldContextVm).toList();

        return new ResponseSuccess<>("Thành công.",result);
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<WorldContextVm> editWorldContext(EditWorldContextDTO dto, int id, User user) {
        var worldContextFound = worldContextRepository.findById(id).orElse(null);
        if(worldContextFound == null){

            LOGGER.error(messageService.getMessage("world-context.log.not-found"));
            throw new WorldContextNotFoundException(messageService.getMessage("world-context.not-found"));
        }

        worldContextFound.setDescription(dto.getDescription());
        worldContextFound.setTitle(dto.getTitle());
        worldContextFound.setUpdatedAt(LocalDateTime.now());

        var save = worldContextRepository.save(worldContextFound);

        var result = mappingService.getWorldContextVm(save);

        LOGGER.info(messageService.getMessage("world-context.log.edit.success"),user.getId(),result.getId());
        return new ResponseSuccess<>(messageService.getMessage("world-context.edit.success"),result);
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<Boolean> deleteWorldContext(int id, User user) {
        return null;
    }

    @Override
    public Optional<WorldContext> findById(int id) {
        return worldContextRepository.findById(id);
    }

    @Override
    @Cacheable(value = CACHE_VALUE, key = "#id")
    public ResponseSuccess<WorldContextVm> getById(int id) {
        var worldContextFound = worldContextRepository.findById(id).orElse(null);
        if(worldContextFound == null){

            LOGGER.error(messageService.getMessage("world-context.log.not-found"));
            throw new WorldContextNotFoundException(messageService.getMessage("world-context.not-found"));
        }

        var result = mappingService.getWorldContextVm(worldContextFound);

        return new ResponseSuccess<>("Thành công.",result);
    }
}
