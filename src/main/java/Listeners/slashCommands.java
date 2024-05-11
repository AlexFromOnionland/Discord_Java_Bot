package Listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.apache.http.util.VersionInfo;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class slashCommands extends ListenerAdapter {

    private TextChannel USER_CHANNEL;
    private String MESSAGE_TYPE = "text";
    private String MESSAGE_COLOR = "#000000";


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        String command = event.getName().toLowerCase();

        switch (command) {
            case "ping":
            {
                List<URL> urlList = new ArrayList<>();
                List<Long> time_in_ms = new ArrayList<>();

                try {
                    // Add URLs to the list
                    urlList.add(new URL("https://discord.com"));
                    urlList.add(new URL("https://google.com"));
                    urlList.add(new URL("https://github.com"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (URL url : urlList) {
                    Long StartTimer=System.currentTimeMillis();
                    try {
                        InetAddress address = InetAddress.getByName(url.getHost());
                        if(address.isReachable(5000))
                        {
                            Long StopTimer=System.currentTimeMillis();
                            time_in_ms.add(StopTimer-StartTimer);
                        }
                        else
                        {
                            time_in_ms.add(9999L);
                        }

                    } catch (Exception e) {
                        time_in_ms.add(-1L);
                    }
                }


                EmbedBuilder Eping = new EmbedBuilder();
                Eping.setColor(new Color(255, 255, 255));
                Eping.setTitle("Ping");
                Eping.setAuthor(event.getJDA().getSelfUser().getName() ,null, event.getJDA().getSelfUser().getAvatarUrl());
                Eping.setDescription("Latency test with Internet sites");

                for(int i=0;i<urlList.size();i++)
                {
                    Eping.addField("latency for "+urlList.get(i).getHost(),time_in_ms.get(i).toString()+" ms",false);
                }

                Eping.setFooter("send by "+event.getUser().getName(), event.getUser().getAvatarUrl() );
                Eping.setTimestamp(Instant.now());

                event.replyEmbeds(Eping.build()).queue();
            }
                break;

            case "info":
            {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(new Color(17, 155, 229));
                eb.setTitle("Info Page");
                eb.setAuthor(event.getJDA().getSelfUser().getName() ,null, event.getJDA().getSelfUser().getAvatarUrl());
                try {
                   
                    String pomFilePath = "pom.xml";
                    MavenXpp3Reader reader = new MavenXpp3Reader();
                    Model model = reader.read(new FileReader(pomFilePath));
                    String JDA_version = model.getDependencies().get(1).getVersion();
                    String LAVA_version = model.getDependencies().get(2).getVersion();

                    eb.setDescription("dependencies versions");
                    eb.addField("JDA","**ver. "+JDA_version+"**",true);
                    eb.addField("LavaPlayer","**ver. "+LAVA_version+"**",true);

                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }

                eb.setFooter("send by "+event.getUser().getName(), event.getUser().getAvatarUrl() );
                eb.setTimestamp(Instant.now());
                event.replyEmbeds(eb.build()).queue();
            }break;

            case "send":
            {
                this.USER_CHANNEL = event.getOption("channel").getAsChannel().asTextChannel();
                this.MESSAGE_TYPE = event.getSubcommandName();
                if (this.MESSAGE_TYPE.equals("embed")) {
                    this.MESSAGE_COLOR = event.getOption("color").getAsString();
                }

                if (!this.USER_CHANNEL.canTalk()) {
                    event.reply("can ot sent message to this channel. Has no permission to see it").setEphemeral(true).queue();
                } else {
                    TextInput user_prompt = TextInput.create("user_prompt", "send message trough bot", TextInputStyle.PARAGRAPH).setPlaceholder("Your content go here").setMinLength(1).setMaxLength(1000).setRequired(true).build();
                    Modal modal = Modal.create("bot_send_command", "Create message").addActionRow(new ItemComponent[]{user_prompt}).build();
                    event.replyModal(modal).queue();
                }
            }
            break;

            default:
                event.reply(event.getName() +" is command that we still working on").queue();
                break;


        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("bot_send_command")) {
            ModalMapping userinfo = event.getValue("user_prompt");
            String String_user_prompt = userinfo.getAsString();
            if (this.MESSAGE_TYPE.equals("embed")) {
                EmbedBuilder modEB = new EmbedBuilder();
                modEB.setDescription(String_user_prompt);
                Color user_color = Color.decode(this.MESSAGE_COLOR);
                modEB.setColor(new Color(user_color.getRed(), user_color.getGreen(), user_color.getBlue()));
                modEB.setFooter("send by " + event.getUser().getName(), event.getUser().getAvatarUrl());
                modEB.setTimestamp(Instant.now());
                modEB.build();
                this.USER_CHANNEL.sendMessageEmbeds(modEB.build(), new MessageEmbed[0]).queue();
            } else {
                this.USER_CHANNEL.sendMessage(String_user_prompt + "\n\n-------------------------\nmessage was by **" + event.getUser().getName() + "**").queue();
            }

            event.reply("message was sent to " + this.USER_CHANNEL.getAsMention()).setEphemeral(true).queue();
        }













    }
}
