package gr.hua;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

/**
 * Created by rg on 13/06/16.
 */
public class RetrieveUserAttributes {

    public LdapContext getLdapContext(){
        LdapContext ctx = null;
        try{
            Hashtable<String,String> env = new Hashtable <String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.SECURITY_AUTHENTICATION, "Simple");
            env.put(Context.SECURITY_PRINCIPAL, "ipt@hua.gr");
            env.put(Context.SECURITY_CREDENTIALS, "5fet3e$2");
            env.put(Context.PROVIDER_URL, "ldap://earth.hua.gr:3268");
            ctx = new InitialLdapContext(env, null);
            System.out.println("Connection Successful.");
        }catch(NamingException nex){
            System.out.println("LDAP Connection: FAILED");
            nex.printStackTrace();
        }
        return ctx;
    }

    public String getUserBasicAttributes(String username, LdapContext ctx) {
        String user=null;
        try {

            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String[] attrIDs = { "distinguishedName",
                    "sn",
                    "givenname",
                    "mail",
                    "telephonenumber",
                    "department",
                    "title"};
            constraints.setReturningAttributes(attrIDs);
            //First input parameter is search bas, it can be "CN=Users,DC=YourDomain,DC=com"
            //Second Attribute can be uid=username
            NamingEnumeration answer = ctx.search("DC=hua,DC=gr", "sAMAccountName="
                    + username, constraints);
            if (answer.hasMore()) {
                Attributes attrs = ((SearchResult) answer.next()).getAttributes();
                System.out.println("distinguishedName "+ attrs.get("distinguishedName"));
                System.out.println("givenname "+ attrs.get("givenname"));
                System.out.println("sn "+ attrs.get("sn"));
                System.out.println("mail "+ attrs.get("mail"));
                System.out.println("department "+ attrs.get("department"));
                System.out.println("title "+ attrs.get("title"));

            }else{
                throw new Exception("Invalid User");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }


}
