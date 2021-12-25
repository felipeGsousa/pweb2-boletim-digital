package br.edu.ifpb.pweb2.boletimDigital;

import br.edu.ifpb.pweb2.boletimDigital.filter.LoginFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BoletimDigitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoletimDigitalApplication.class, args);
	}


	@Bean
	public FilterRegistrationBean<LoginFilter> filterRegistrationBean() {
		FilterRegistrationBean<LoginFilter> registrationBean = new FilterRegistrationBean<LoginFilter>();
		LoginFilter customURLFilter = new LoginFilter();

		registrationBean.setFilter(customURLFilter);
		registrationBean.addUrlPatterns("/");
		registrationBean.addUrlPatterns("/home/*");
		registrationBean.addUrlPatterns("/estudantes/*");
		registrationBean.setOrder(1);
		return registrationBean;
	}

}
