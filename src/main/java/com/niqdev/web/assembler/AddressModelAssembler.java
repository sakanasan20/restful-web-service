package com.niqdev.web.assembler;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.niqdev.web.controller.UserController;
import com.niqdev.web.dto.response.AddressResponseDto;

@Component
public class AddressModelAssembler 
		implements RepresentationModelAssembler<AddressResponseDto, EntityModel<AddressResponseDto>> {

	@Override
	public EntityModel<AddressResponseDto> toModel(AddressResponseDto address) {
		return EntityModel.of(address,
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
						.getAddress(address.getUserId(), address.getAddressId())).withSelfRel(),
				WebMvcLinkBuilder
						.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAddresses(address.getUserId()))
						.withRel("addresses"),
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(address.getUserId()))
						.withRel("user"));
	}

	public CollectionModel<EntityModel<AddressResponseDto>> toCollectionModel(List<AddressResponseDto> addresses) {
		List<EntityModel<AddressResponseDto>> models = addresses.stream().map(this::toModel).toList();
		
		String userId = addresses.isEmpty() ? "" : addresses.get(0).getUserId();
		
		return CollectionModel.of(models,
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAddresses(userId))
						.withSelfRel(),
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(userId))
						.withRel("user"));
	}
}
