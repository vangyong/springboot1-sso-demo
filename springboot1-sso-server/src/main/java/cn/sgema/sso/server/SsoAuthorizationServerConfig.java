package cn.sgema.sso.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class SsoAuthorizationServerConfig  extends AuthorizationServerConfigurerAdapter{
	
//	@Autowired
//    private AuthenticationManager authenticationManager;
//	
//	@Autowired
//	private DataSource dataSource;
	
	//配置客户端详情服务（ClientDetailsService）
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
		.withClient("segema1")
		.secret("segemasecret1")
		.authorizedGrantTypes("authorization_code","refresh_token")
		.scopes("all")
		.and()
		.withClient("segema2")
		.secret("segemasecret2")
		.authorizedGrantTypes("authorization_code","refresh_token")
		.scopes("all");
		
		//本地数据库查找
//		clients.withClientDetails(clientDetails());
	}
	
//	@Bean
//	public ClientDetailsService clientDetails() {
//		return new JdbcClientDetailsService(dataSource);
//	}

	//配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(jwtTokenStore())
		.accessTokenConverter(jwtAccessTokenConverter());
		
//		endpoints.authenticationManager(authenticationManager);
//		endpoints.tokenStore(jwtTokenStore());
//        // 配置TokenServices参数
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setTokenStore(endpoints.getTokenStore());
//        tokenServices.setSupportRefreshToken(false);
//        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
//        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
//        tokenServices.setAccessTokenValiditySeconds( (int) TimeUnit.DAYS.toSeconds(30)); // 30天
//        endpoints.tokenServices(tokenServices);
	}
	
	//配置令牌端点(Token Endpoint)的安全约束
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("isAuthenticated()");
	}
	
	
	@Bean
	public TokenStore jwtTokenStore() {
		
		//return new JdbcTokenStore(dataSource);
		return new JwtTokenStore(jwtAccessTokenConverter());
	}
	
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		 JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		 accessTokenConverter.setSigningKey("segema");
		 return accessTokenConverter;
	}
	
//	@Bean
//	@ConditionalOnMissingBean(name="jwtTokenEnhancer")
//	public TokenEnhancer jwtTokenEnhancer() {
//		 return new SegemaJwtEnhancer();
//	}
	
	
//	@Bean
//    @Primary
//    public DefaultTokenServices tokenServices() {
//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setTokenStore(jwtTokenStore());
//        defaultTokenServices.setSupportRefreshToken(true);
//        return defaultTokenServices;
//    }

}
