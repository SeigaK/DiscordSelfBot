package net.notfab.discord.selfbot;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.File;

/**
 * DiscordSelfBot - http://notfab.net/
 * Created by Fabricio20 on 4/10/2016.
 */
public class Main {

    @Getter private static Main Instance;
    @Getter private JDA JDA;

    @Getter @Setter
    private boolean Idle;

    @Getter private final String OriginalName;

    public Main() {
        Instance = this;
        JSONObject o = FileUtils.readFile(new File("BotConfig.json"));
        if(!o.has("Email") || !o.has("Password")) {
            System.out.print("Error: File Is Missing Email and Password Fields.");
            System.exit(-1);
        }
        try {
            String email = o.getString("Email");
            String pwd = o.getString("Password");
            JDA = new JDABuilder().setEmail(email).setPassword(pwd).setAutoReconnect(true).buildBlocking();
        } catch (LoginException ex1) {
            System.out.println("Error: Invalid Credentials.");
            System.exit(-2);
        } catch (InterruptedException ex2) {
            ex2.printStackTrace();
            System.exit(-3);
        }
        OriginalName = JDA.getSelfInfo().getUsername();
        JDA.addEventListener(new CommandListener());
        JDA.addEventListener(new MessageListener());
        while(true) {} // Don't die
    }

}
