package com.example.kunuz;

import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.util.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KunUzApplication {

	public static void main(String[] args) {
		SpringApplication.run(KunUzApplication.class, args);
		System.out.println(JwtUtil.encode(3, ProfileRole.MODERATOR));
	}

}
