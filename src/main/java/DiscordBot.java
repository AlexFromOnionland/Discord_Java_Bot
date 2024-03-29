import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class DiscordBot {


    public static void main(String[] args) throws LoginException {
        Dotenv dotenv = Dotenv.load();

        String TOKEN = dotenv.get("JAVA_TOKEN");
        JDABuilder JDA = JDABuilder.createDefault(TOKEN);

JDA.build();

    }
}
