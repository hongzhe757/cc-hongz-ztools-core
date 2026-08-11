package cc.hongz.ztools.core.net;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * 账号密码形式的{@link Authenticator}.
 *
 * @author looly
 * @since 5.7.2
 */
public class UserPassAuthenticator extends Authenticator {

    /**
     * 用户名.
     */
    private final String user;
    /**
     * 密码.
     */
    private final char[] pass;

    /**
     * 构造.
     *
     * @param user 用户名
     * @param pass 密码
     */
    public UserPassAuthenticator(final String user, final char[] pass) {
        this.user = user;
        this.pass = pass;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.user, this.pass);
    }
}
