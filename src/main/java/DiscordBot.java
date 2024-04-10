import Listeners.*;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class DiscordBot {


    public static void main(String[] args) throws LoginException {
        Dotenv dotenv = Dotenv.load();

        String TOKEN = dotenv.get("JAVA_TOKEN");
        JDABuilder jdaBuilder = JDABuilder.createDefault(TOKEN);

        JDA jda =jdaBuilder
                .enableIntents(GatewayIntent.GUILD_MESSAGES,GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new EventListener(),new slashCommands())
                .setActivity(Activity.watching("dzieci w lesie"))
                .build();

       // jda.updateCommands().addCommands().queue();
        //jda.upsertCommand("random-fact","send random fact about everything!").queue();
        jda.upsertCommand("ping","send pong").queue();
        jda.upsertCommand("info","send info").queue();



    }
}
