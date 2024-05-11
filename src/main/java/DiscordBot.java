import Listeners.*;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class DiscordBot {


    public static void main(String[] args) throws LoginException  {
        Dotenv dotenv = Dotenv.load();

        String TOKEN = dotenv.get("JAVA_TOKEN");
        JDABuilder jdaBuilder = JDABuilder.createDefault(TOKEN);

        JDA jda =jdaBuilder
                .enableIntents(GatewayIntent.GUILD_MESSAGES,GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new EventListener(),new slashCommands())
                .setActivity(Activity.watching("dzieci w lesie"))
                .build();





        jda.updateCommands().addCommands().queue();

        jda.upsertCommand("ping","send pong").queue();
        jda.upsertCommand("info","send info").queue();


        OptionData channel = new OptionData(OptionType.CHANNEL, "channel", "the channel where you wart to send message", true)
                .setChannelTypes(ChannelType.TEXT,ChannelType.NEWS);
        OptionData color = new OptionData(OptionType.STRING, "color", "define color of embed", true);

        color.addChoice("blue [info]", "#4287f5")
                .addChoice("red [error]", "#f54242")
                .addChoice("green [ok]", "#42f554")
                .addChoice("yellow [warning]", "#f5ef42")
                .addChoice("orange [fix]", "#d66d11");

        SubcommandData embed = new SubcommandData("embed", "send as embed").addOptions(channel,color);
        SubcommandData text = new SubcommandData("text", "send as text").addOptions(channel);


        jda.upsertCommand("send","send message trough bot ").addSubcommands(embed,text).queue();




    }




}
