package com.protasknewbbbx.service.mapper;

import com.protasknewbbbx.domain.Category;
import com.protasknewbbbx.domain.Task;
import com.protasknewbbbx.domain.User;
import com.protasknewbbbx.service.dto.CategoryDTO;
import com.protasknewbbbx.service.dto.TaskDTO;
import com.protasknewbbbx.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    @Mapping(target = "assignee", source = "assignee", qualifiedByName = "userLogin")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    TaskDTO toDto(Task s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);
}
