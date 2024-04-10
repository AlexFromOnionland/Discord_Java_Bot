package Listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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


public static  long test_latency(String URLs)
{

        long StartTimer=System.currentTimeMillis();
        try
        {
            InetAddress address=InetAddress.getByName(URLs);
            if(address.isReachable(10000))
            {
                long StopTimer=System.currentTimeMillis();
                return StopTimer-StartTimer;
            }
        }
        catch (Exception e)
        {
            return 9999L;
        }

    return -1L;
}

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
            }

                break;

            default:
                event.reply(event.getName() +" is command that we still working on").queue();
                break;


        }
    }
}
