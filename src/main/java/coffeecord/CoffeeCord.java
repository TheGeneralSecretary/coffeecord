package coffeecord;

import config.Config;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class CoffeeCord {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoffeeCord.class);

    public static void run(String token) throws LoginException {
        JDABuilder.createLight(token, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
                .setActivity(Activity.listening("Coffee"))
                .addEventListeners(new Listener())
                .build();
    }

    public static void main(String[] args) {
        try {
            String token = Config.get("token");
            run(token);
        } catch (Exception e) {
        	LOGGER.error(e.getMessage());
        }
    }
}
