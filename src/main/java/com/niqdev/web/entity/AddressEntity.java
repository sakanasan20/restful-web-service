package com.niqdev.web.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "app_addresses")
public class AddressEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String addressId;
	
	@Column(nullable = false, length = 100)
	private String street;
    
	@Column(nullable = false, length = 15)
    private String city;
    
	@Column(nullable = false, length = 7)
    private String postalCode;
    
	@Column(nullable = false, length = 15)
    private String country;
    
	@Column(nullable = false, length = 10)
    private String type;
    
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    
    @ToString.Include(name = "userId")
    public String getUserId() {
        return user != null ? user.getUserId() : null;
    }
	
}
