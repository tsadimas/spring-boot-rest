package gr.hua;


import javax.naming.directory.Attributes;
import javax.sql.DataSource;
import gr.hua.models.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    DataSource dataSource;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user1").password("secret1").roles("USER")
//                .and()
//                .withUser("user2").password("secret2").roles("USER");



//        auth.jdbcAuthentication().dataSource(dataSource)
//        .usersByUsernameQuery("select username,password, enabled from users where username=?")
//        .authoritiesByUsernameQuery("select username, role from user_roles where username=?");


        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource("ldap://earth.hua.gr:3268/dc=hua,dc=gr");
        contextSource.setUserDn("ipt@hua.gr");
        contextSource.setPassword("5fet3e$2");
        //contextSource.setReferral("follow");
        contextSource.afterPropertiesSet();

        LdapAuthenticationProviderConfigurer<AuthenticationManagerBuilder> ldapAuthenticationProviderConfigurer = auth.ldapAuthentication();

        ldapAuthenticationProviderConfigurer
                .userSearchFilter("(sAMAccountName={0})")
                //.userSearchBase("ou=people")
                .contextSource(contextSource);



    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.authorizeRequests().antMatchers("/employees").permitAll();
//         http.authorizeRequests().anyRequest().fullyAuthenticated();
//        http.httpBasic();
//        http.csrf().disable();



        http.authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin();
    }

}