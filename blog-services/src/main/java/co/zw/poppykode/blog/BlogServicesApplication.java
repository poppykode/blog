package co.zw.poppykode.blog;

import co.zw.poppykode.blog.entity.Role;
import co.zw.poppykode.blog.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BlogServicesApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(BlogServicesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String adminRole = "ROLE_ADMIN";
		String userRole = "ROLE_USER";
		if(!roleRepository.existsByName(adminRole)){
			Role roleAdmin = new Role();
			roleAdmin.setName(adminRole);
			roleRepository.save(roleAdmin);
		}
		if(!roleRepository.existsByName(userRole)){
			Role roleUser = new Role();
			roleUser.setName(userRole);
			roleRepository.save(roleUser);
		}
	}

}
