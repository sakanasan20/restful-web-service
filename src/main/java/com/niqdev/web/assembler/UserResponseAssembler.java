package com.niqdev.web.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.niqdev.web.controller.UserController;
import com.niqdev.web.dto.response.UserResponseDto;

@Component
public class UserResponseAssembler implements RepresentationModelAssembler<UserResponseDto, EntityModel<UserResponseDto>> {

	@Override
	public EntityModel<UserResponseDto> toModel(UserResponseDto user) {
		return EntityModel.of(user, 
				linkTo(methodOn(UserController.class).getUser(user.getUserId())).withSelfRel(),
				linkTo(methodOn(UserController.class).getUsers(0, 25)).withRel("users"),
			    linkTo(methodOn(UserController.class).updateUser(user.getUserId(), null)).withRel("update"),
			    linkTo(methodOn(UserController.class).deleteUser(user.getUserId())).withRel("delete"),
			    linkTo(methodOn(UserController.class).createUser(null)).withRel("create"), 
				linkTo(methodOn(UserController.class).getAddresses(user.getUserId())).withRel("addresses"));
	}

	public PagedModel<EntityModel<UserResponseDto>> toPagedModel(Page<UserResponseDto> users) {
		List<EntityModel<UserResponseDto>> models = users.stream().map(this::toModel).toList();

		PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
				users.getSize(), 			// 每頁大小
				users.getNumber(), 			// 當前頁碼（0-based）
				users.getTotalElements(), 	// 總筆數
				users.getTotalPages() 		// 總頁數
		);

		PagedModel<EntityModel<UserResponseDto>> pagedModel = PagedModel.of(models, metadata);

		// 當前分頁 links
		pagedModel
				.add(linkTo(methodOn(UserController.class).getUsers(users.getNumber(), users.getSize())).withSelfRel());

		// 下頁分頁 links
		if (users.hasNext()) {
			pagedModel.add(linkTo(methodOn(UserController.class).getUsers(users.getNumber() + 1, users.getSize()))
					.withRel("next"));
		}

		// 上頁分頁 links
		if (users.hasPrevious()) {
			pagedModel.add(linkTo(methodOn(UserController.class).getUsers(users.getNumber() - 1, users.getSize()))
					.withRel("prev"));
		}

		return pagedModel;
	}
}
