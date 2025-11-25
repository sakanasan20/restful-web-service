package com.niqdev.web.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.niqdev.web.controller.UserController;
import com.niqdev.web.dto.response.OperationResponseDto;

@Component
public class OperationResponseAssembler
		implements RepresentationModelAssembler<OperationResponseDto, EntityModel<OperationResponseDto>> {

	@Override
	public EntityModel<OperationResponseDto> toModel(OperationResponseDto dto) {
		return EntityModel.of(dto,
				// 回到 user 列表
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUsers(0, 25))
						.withRel("users"),
				// 可以建立新 user
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).createUser(null))
						.withRel("create"));
	}
}
