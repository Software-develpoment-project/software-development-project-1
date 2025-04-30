package codefusion.softwareproject1.service.mapper;

/**
 * Generic mapper interface for converting between entities and DTOs.
 * Follows Single Responsibility Principle by isolating mapping logic.
 * 
 * @param <E> Entity type
 * @param <D> DTO type
 */
public interface EntityMapper<E, D> {
    
    /**
     * Converts an entity to a DTO.
     * 
     * @param entity the entity to convert
     * @return the corresponding DTO
     */
    D toDto(E entity);
    
    /**
     * Converts a DTO to an entity.
     * 
     * @param dto the DTO to convert
     * @return the corresponding entity
     */
    E toEntity(D dto);
    
    /**
     * Updates an entity with data from a DTO.
     * 
     * @param entity the entity to update
     * @param dto the DTO containing update data
     * @return the updated entity
     */
    E updateEntityFromDto(D dto, E entity);
} 