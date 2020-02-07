package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class BattleshipApplication extends SpringBootServletInitializer{
		public static void main(String[] args) {
		SpringApplication.run(BattleshipApplication.class, args);
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		}

		@Autowired
		PasswordEncoder passwordEncoder;

		@Bean
		public CommandLineRunner initData(PlayerRepository repositoryPlayer,
									  GameRepository repositoryGame,
									  GamePlayerRepository repositoryGamePlayer,
									  ShipRepository shipRepository,
									  SalvoRepository salvoRepository,
                                      ScoreRepository scoreRepository) {

		return (args) -> {
			// Players
			Player heike = new Player("Heisel", passwordEncoder().encode("luppe"), "I am a winner!");
			Player hans = new Player("Hänsel", passwordEncoder().encode("nase"), "I am invincible!");
			Player grete = new Player("Gretel", passwordEncoder().encode("dose"), "Here to stay!");

			// Games
			Game game1 = new Game(new Date());
			Game game2 = new Game(new Date());
			Game game3 = new Game(new Date());

			// GamePlayers
			GamePlayer gamePlayer1 = new GamePlayer(heike, game1);
			GamePlayer gamePlayer2 = new GamePlayer(hans, game1);
			GamePlayer gamePlayer3 = new GamePlayer(grete, game2);
			//GamePlayer gamePlayer4 = new GamePlayer(hans, game2);
			GamePlayer gamePlayer5 = new GamePlayer(heike, game3);
			GamePlayer gamePlayer6 = new GamePlayer(grete, game3);

			// ShipLocations
			List<String> shipLocList1 = Arrays.asList("A1","A2","A3");
			List<String> shipLocList2 = Arrays.asList("B1","B2","B3");
			List<String> shipLocList3 = Arrays.asList("F2","F3");
			List<String> shipLocList4 = Arrays.asList("D5","D6");
			List<String> shipLocList5 = Arrays.asList("E10","F10","G10");
			List<String> shipLocList6 = Arrays.asList("H7","H8");

			// ships
			Ship ship1 = new Ship("Destroyer", shipLocList1);
			Ship ship2 = new Ship("Submarine", shipLocList2);
			Ship ship3 = new Ship("Carrier", shipLocList3);
			Ship ship4 = new Ship("Carrier", shipLocList4);
			Ship ship5 = new Ship("Destroyer", shipLocList5);
			Ship ship6 = new Ship("Carrier", shipLocList6);

			// add ships to gamePlayers
			gamePlayer1.addShip(ship1);
			gamePlayer2.addShip(ship2);
			gamePlayer1.addShip(ship3);
			gamePlayer2.addShip(ship4);
			gamePlayer3.addShip(ship5);
			gamePlayer3.addShip(ship6);

			List<String> salvoLocList1 = Arrays.asList("B2","B3","B4");
			List<String> salvoLocList2 = Arrays.asList("B2","B3","B4");
			List<String> salvoLocList3 = Arrays.asList("F5","F6", "F7");
			List<String> salvoLocList4 = Arrays.asList("D5","D6", "D7");
			List<String> salvoLocList5 = Arrays.asList("F10","D2", "B10");
			List<String> salvoLocList6 = Arrays.asList("A1","B2", "C3");

			// create salvoes
			Salvo salvo1 = new Salvo (1, salvoLocList1);
			Salvo salvo2 = new Salvo (1, salvoLocList2);
			Salvo salvo3 = new Salvo (2, salvoLocList3);
			Salvo salvo4 = new Salvo (2, salvoLocList4);
			Salvo salvo5 = new Salvo (1, salvoLocList5);
			Salvo salvo6 = new Salvo (1, salvoLocList6);

			// add salvoes to gamePlayers
			gamePlayer1.addSalvo(salvo1);
			gamePlayer2.addSalvo(salvo2);
			gamePlayer1.addSalvo(salvo3);
			gamePlayer2.addSalvo(salvo4);
			gamePlayer3.addSalvo(salvo5);
			gamePlayer3.addSalvo(salvo6);



			// save stuff

			repositoryPlayer.save(heike);
			repositoryGame.save(game1);
			repositoryPlayer.save(hans);
			repositoryGame.save(game1);
			repositoryPlayer.save(grete);
			repositoryGame.save(game2);
			//repositoryPlayer.save(hans);
			//repositoryGame.save(game2);
			repositoryPlayer.save(heike);
			repositoryGame.save(game3);
			repositoryPlayer.save(grete);
			repositoryGame.save(game3);

			// create scores
			Score score1 = new Score(1.0, game1, heike);
			Score score2 = new Score(0.0, game1, hans);
			Score score3 = new Score (0.5, game2, grete);
			Score score4 = new Score (0.5, game2, hans);
			Score score5 = new Score (1.0, game3, heike);
			Score score6 = new Score (0.0, game3, grete);

			// save stuff to repositories

			scoreRepository.save(score1);
			scoreRepository.save(score2);
			scoreRepository.save(score3);
			scoreRepository.save(score4);
			scoreRepository.save(score5);
			scoreRepository.save(score6);

			repositoryGame.save(game1);
			repositoryPlayer.save(heike);
			repositoryPlayer.save(hans);

			repositoryGame.save(game2);
			repositoryPlayer.save(grete);
			repositoryPlayer.save(hans);

			repositoryGame.save(game3);
			repositoryPlayer.save(heike);
			repositoryPlayer.save(grete);

			repositoryGamePlayer.save(gamePlayer1);
			repositoryGamePlayer.save(gamePlayer2);
			repositoryGamePlayer.save(gamePlayer3);
			//repositoryGamePlayer.save(gamePlayer4);
			repositoryGamePlayer.save(gamePlayer5);
			repositoryGamePlayer.save(gamePlayer6);

			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);

			salvoRepository.save(salvo1);
			salvoRepository.save(salvo2);
			salvoRepository.save(salvo3);
			salvoRepository.save(salvo4);

			System.out.println("I am ready.");

		};
	}


	@Bean
	public FilterRegistrationBean simpleCorsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		// *** URL below needs to match the Vue client URL and port ***
		config.setAllowedOrigins(Collections.singletonList("http://localhost:8081"));
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}





	@CrossOrigin(origins = "http://localhost:8081/")
	@Configuration
	class WebAuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {

		@Autowired
		private PlayerRepository repositoryPlayer;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(inputUserName-> {
				System.out.println(inputUserName);
				Player player = repositoryPlayer.findByUserName(inputUserName);
				if (player != null) {
					return new User(player.getUserName(), player.getPassword(),
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException("Unknown user: " + inputUserName);
				}
			});
		}
		}
@CrossOrigin(origins = "http://localhost:8081/")
	@EnableWebSecurity
	@Configuration

	class WebAccessConfig extends WebSecurityConfigurerAdapter {

	@CrossOrigin(origins = "http://localhost:8081/")
		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
					.and().authorizeRequests()
                    .antMatchers("/**").permitAll()
					.antMatchers("/web/index.html*").permitAll()
					.antMatchers("/web/login.js*").permitAll()
					.antMatchers("/web/games.html*").permitAll()
					.antMatchers("/web/games.js*").permitAll()
					.antMatchers("/web/game.html*").permitAll()
					.antMatchers("/web/game.js*").permitAll()
					.antMatchers("/web/leaderboard.html*").permitAll()
					.antMatchers("/web/style.css*").permitAll()
					.antMatchers("/api/register").permitAll()
					.antMatchers("/rest/players").permitAll()
					.antMatchers("/api/game_view*").permitAll()
					.antMatchers("/api/login").permitAll()
					.antMatchers("/api/game").permitAll()
					.antMatchers("/api/games").permitAll()
					.antMatchers("/api/players/**").permitAll()
					.anyRequest().authenticated().and()

			.formLogin()
					.usernameParameter("userName")
					.passwordParameter("password")
					.loginPage("/api/login").and()

			.logout().logoutUrl("/api/logout");

			// turn off checking for CSRF tokens
			http.csrf().disable();

			// if user is not authenticated, just send an authentication failure response
			http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

			// if login is successful, just clear the flags asking for authentication
			http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

			// if login fails, just send an authentication failure response
			http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_CONFLICT));

			// if logout is successful, just send a success response
			http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
		}

		private void clearAuthenticationAttributes(HttpServletRequest request) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			}
		}


	}



}
