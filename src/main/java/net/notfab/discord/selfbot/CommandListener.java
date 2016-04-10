package net.notfab.discord.selfbot;

import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.exceptions.PermissionException;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.AvatarUtil;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * DiscordSelfBot - http://notfab.net/
 * Created by Fabricio20 on 4/10/2016.
 */
public class CommandListener extends ListenerAdapter {

    private final String LENNY = "( ͡° ͜ʖ ͡°)";
    private final String SHRUG = "¯\\_(ツ)_/¯";

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if(!e.getAuthor().getId().equals(Main.getInstance().getJDA().getSelfInfo().getId())) {
            return;
        }
        String[] args = e.getMessage().getContent().split(" ");
        String cmd = args[0];
        try {
            Thread.sleep(200); // Sleep So Discord Doesn't Hate On Us
        } catch (InterruptedException ex) {}

        if(cmd.equalsIgnoreCase("/lenny")) {
            e.getMessage().updateMessage(this.LENNY);
        } else if(cmd.equalsIgnoreCase("/shrug")) {
            e.getMessage().updateMessage(this.SHRUG);
        } else if(cmd.equalsIgnoreCase("/idle")) {
            Main.getInstance().setIdle(!Main.getInstance().isIdle());
            Main.getInstance().getJDA().getAccountManager().setIdle(Main.getInstance().isIdle());
            e.getMessage().updateMessage((Main.getInstance().isIdle() ? "Be Right Back" : ":back:"));
        } else if(cmd.equalsIgnoreCase("/abal")) {
            String rest = argsToString(args, 1);
            e.getMessage().updateMessage("<@98295630480314368> " + rest);
        } else if(cmd.equalsIgnoreCase("/stop")) {
            e.getMessage().deleteMessage();
            System.exit(0);
        } else if(cmd.equalsIgnoreCase("/name")) {
            if(args.length == 0) {
                e.getMessage().updateMessage("Error: You didn't specify a name!");
            } else {
                String preChange = Main.getInstance().getJDA().getSelfInfo().getUsername();
                try {
                    Main.getInstance().getJDA().getAccountManager().setUsername(argsToString(args, 1)).update();
                    e.getMessage().updateMessage("**__Username:__** `" + preChange + "` -> `" + argsToString(args, 1) + "`");
                } catch (Exception ex) {
                    e.getMessage().updateMessage("**__Error:__** `>" + ex.getMessage() + "`");
                }
            }
        } else if(cmd.equalsIgnoreCase("/revert")) {
            String preChange = Main.getInstance().getJDA().getSelfInfo().getUsername();
            String originalName = Main.getInstance().getOriginalName();
            try {
                Main.getInstance().getJDA().getAccountManager().setUsername(Main.getInstance().getOriginalName()).update();
                e.getMessage().updateMessage("**__Username:__** `" + preChange + "` -> `" + originalName + "`");
            } catch (Exception ex) {
                e.getMessage().updateMessage("**__Error:__** `>" + ex.getMessage() + "`");
            }
        } else if(cmd.equalsIgnoreCase("/purge")) {
            if(args.length == 0) {
                e.getMessage().updateMessage("**__Error:__** `>No User Selected!`");
            } else if(args.length == 1) {
                if(e.getMessage().getMentionedUsers().size() == 0) {
                    e.getMessage().updateMessage("**__Error:__** `>No User Selected!`");
                    return;
                }
                try {
                    User u = e.getMessage().getMentionedUsers().get(0);
                    deleter(new MessageHistory(e.getJDA(), e.getChannel()).retrieve(100), u);
                } catch (PermissionException ex1) {
                    e.getMessage().updateMessage("**__Error:__** `>I am not allowed to delete this users messages.`");
                } catch (NullPointerException ex2) {
                    e.getMessage().updateMessage("**__Error:__** `>" + e.getMessage() + "`");
                }
            } else {
                Integer amount;
                try {
                    amount = Integer.parseInt(args[1]);
                    if(amount > 100) {
                        e.getMessage().updateMessage("**__Error:__** `>Number Is Too Big! (Max 100)`");
                    }
                } catch (IllegalArgumentException ex) {
                    e.getMessage().updateMessage("**__Error:__** `>That Is Not A Number!`");
                    return;
                }
                if(e.getMessage().getMentionedUsers().size() == 0) {
                    e.getMessage().updateMessage("**__Error:__** `>No User Selected!`");
                    return;
                }
                try {
                    User u = e.getMessage().getMentionedUsers().get(0);
                    deleter(new MessageHistory(e.getJDA(), e.getChannel()).retrieve(amount), u);
                } catch (PermissionException ex1) {
                    e.getMessage().updateMessage("**__Error:__** `>I am not allowed to delete this users messages.`");
                } catch (NullPointerException ex2) {
                    e.getMessage().updateMessage("**__Error:__** `>" + e.getMessage() + "`");
                }
            }
        } else if(cmd.equalsIgnoreCase("/game")) {
            if(args.length == 0) {
                e.getMessage().updateMessage("**__Error:__** `>No game provided`");
            } else {
                String oldGame = Main.getInstance().getJDA().getSelfInfo().getCurrentGame();
                String newGame = argsToString(args, 1);
                try {
                    Main.getInstance().getJDA().getAccountManager().setGame(newGame);
                    e.getMessage().updateMessage("**__Game:__** `" + (oldGame == null ? "" : oldGame) + "` -> `" + newGame + "`");
                } catch (Exception ex) {
                    e.getMessage().updateMessage("**__Error:__** `>" + ex.getMessage() + "`");
                }
            }
        } else if(cmd.equalsIgnoreCase("/thegame")) {
            e.getMessage().updateMessage("I just lost.");
        } else if(cmd.equalsIgnoreCase("/greentext") || cmd.equalsIgnoreCase("/gt")) {
            String rest = argsToString(args, 1);
            e.getMessage().updateMessage("```css\n>" + rest + "```");
        } else if(cmd.equalsIgnoreCase("/triforce")) {
            String msg = "```\n" +
                    " ▲\n" +
                    "▲ ▲\n" +
                    "```";
            e.getMessage().updateMessage(msg);
        } else if(cmd.equalsIgnoreCase("/newfag")) {
            String msg = "```\n" +
                    "▲\n" +
                    "▲ ▲\n" +
                    "```";
            e.getMessage().updateMessage(msg);
        } else if(cmd.equalsIgnoreCase("/lmgtfy")) {
            if(args.length == 0) {
                e.getMessage().updateMessage("**__Error:__** `>You must specify a term!`");
            } else {
                String q = argsToString(args, 1);
                e.getMessage().updateMessage("http://lmgtfy.com/?q=" + q.replace(" ", "+"));
            }
        } else if(cmd.equalsIgnoreCase("/tinyurl")) {
            if(args.length == 0) {
                e.getMessage().updateMessage("**__Error:__** `>You must specify an url!`");
            } else {
                String url = argsToString(args, 1);
                try {
                    Document doc = Jsoup.connect("http://short.notfab.net/index.php")
                            .ignoreContentType(true).ignoreHttpErrors(true).data("URL", url).post();
                    JSONObject o = new JSONObject(doc.text());
                    if(o.has("error")) {
                        e.getMessage().updateMessage("**__Error:__** `>Error while creating tinyurl!`");
                    }
                    e.getMessage().updateMessage("http://short.notfab.net/" + o.getString("UUID"));
                } catch (Exception ex) {
                    e.getMessage().updateMessage("**__Error:__** `>Error while creating tinyurl!`");
                }
            }
        } else if (cmd.equalsIgnoreCase("/navyseal")) {
            e.getMessage().updateMessage("What the fuck did you just fucking say about me, you little bitch? I’ll have you know I graduated top of my class in the Navy Seals, and I’ve been involved in numerous secret raids on Al-Quaeda, and I have over 300 confirmed kills. I am trained in gorilla warfare and I’m the top sniper in the entire US armed forces. You are nothing to me but just another target. I will wipe you the fuck out with precision the likes of which has never been seen before on this Earth, mark my fucking words. You think you can get away with saying that shit to me over the Internet? Think again, fucker. As we speak I am contacting my secret network of spies across the USA and your IP is being traced right now so you better prepare for the storm, maggot. The storm that wipes out the pathetic little thing you call your life. You’re fucking dead, kid. I can be anywhere, anytime, and I can kill you in over seven hundred ways, and that’s just with my bare hands. Not only am I extensively trained in unarmed combat, but I have access to the entire arsenal of the United States Marine Corps and I will use it to its full extent to wipe your miserable ass off the face of the continent, you little shit. If only you could have known what unholy retribution your little “clever” comment was about to bring down upon you, maybe you would have held your fucking tongue. But you couldn’t, you didn’t, and now you’re paying the price, you goddamn idiot. I will shit fury all over you and you will drown in it. You’re fucking dead, kiddo.");
        } else if (cmd.equalsIgnoreCase("/edgyshit")) {
            e.getMessage().updateMessage("\uD83D\uDC89\uD83D\uDD2A \uD83D\uDC89\uD83D\uDD2A\uD83D\uDC89\uD83D\uDD2Aedgy shit edgY sHit \uD83D\uDD2Athats \uD83D\uDD2Bsome edgy\uD83D\uDC89\uD83D\uDC89 shit right \uD83D\uDD2Ath\uD83D\uDD2A ere\uD83D\uDC89\uD83D\uDC89\uD83D\uDC89 right there \uD83D\uDEAC\uD83D\uDEACif i do ƽaү so my selｆ \uD83D\uDD2Bi say so \uD83D\uDD2B thats what im talking about right there right there (chorus: ʳᶦᵍʰᵗ ᵗʰᵉʳᵉ) mMMMMᎷМ\uD83D\uDD2B \uD83D\uDD2A\uD83D\uDD2A\uD83D\uDD2AНO0ОଠＯOOＯOОଠଠOoooᵒᵒᵒᵒᵒᵒᵒᵒᵒ\uD83D\uDD2A\uD83D\uDD2A\uD83D\uDD2A \uD83D\uDD2B \uD83D\uDC89\uD83D\uDC89 \uD83D\uDD2A\uD83D\uDD2A Edgy shit");
        } else if (cmd.equalsIgnoreCase("/goodshit")) {
            e.getMessage().updateMessage("\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40\uD83D\uDC4C\uD83D\uDC40 good shit go౦ԁ sHit\uD83D\uDC4C thats ✔ some good\uD83D\uDC4C\uD83D\uDC4Cshit right\uD83D\uDC4C\uD83D\uDC4Cthere\uD83D\uDC4C\uD83D\uDC4C\uD83D\uDC4C right✔there ✔✔if i do ƽaү so my self \uD83D\uDCAF i say so \uD83D\uDCAF thats what im talking about right there right there (chorus: ʳᶦᵍʰᵗ ᵗʰᵉʳᵉ) mMMMMᎷМ\uD83D\uDCAF \uD83D\uDC4C\uD83D\uDC4C \uD83D\uDC4CНO0ОଠOOOOOОଠଠOoooᵒᵒᵒᵒᵒᵒᵒᵒᵒ\uD83D\uDC4C \uD83D\uDC4C\uD83D\uDC4C \uD83D\uDC4C \uD83D\uDCAF \uD83D\uDC4C \uD83D\uDC40 \uD83D\uDC40 \uD83D\uDC40 \uD83D\uDC4C\uD83D\uDC4CGood shit");
        } else if (cmd.equalsIgnoreCase("/apache")) {
            e.getMessage().updateMessage("I sexually Identify as an Attack Helicopter. Ever since I was a boy I dreamed of soaring over the oilfields dropping hot sticky loads on disgusting foreigners. People say to me that a person being a helicopter is Impossible and I'm fucking retarded but I don't care, I'm beautiful. I'm having a plastic surgeon install rotary blades, 30 mm cannons and AMG-114 Hellfire missiles on my body. From now on I want you guys to call me \"Apache\" and respect my right to kill from above and kill needlessly. If you can't accept me you're a heliphobe and need to check your vehicle privilege. Thank you for being so understanding.");
        } else if(cmd.equalsIgnoreCase("/daddy")) {
            e.getMessage().updateMessage("\uD83D\uDC9EDaddy\uD83D\uDC9E be nimble\uD83D\uDEB6\uD83C\uDFFB Daddy be quick\uD83C\uDFC3\uD83C\uDFFB\uD83D\uDCA8 Daddy has a rock\uD83D\uDDFF hard dick \uD83C\uDF46\uD83D\uDE0D! 1️⃣cummy\uD83D\uDCA6 2️⃣cummy\uD83D\uDCA6\uD83D\uDCA6 3️⃣cummy\uD83D\uDCA6\uD83D\uDCA6\uD83D\uDCA6 4️⃣\uD83D\uDCA6\uD83D\uDCA6\uD83D\uDCA6\uD83D\uDCA6! Daddy cums\uD83D\uDCA6 so much he can't cum any more\uD83D\uDE48\uD83D\uDE49\uD83D\uDE4A! Ghost cummy\uD83D\uDC7B\uD83D\uDCA6 Ghost cummy\uD83D\uDC7B\uD83D\uDCA6 don't be scared❌\uD83D\uDE16❌! There's always more cummies\uD83D\uDCA6\uD83D\uDC45 that can be shared\uD83D\uDC6C! Daddy makes me ☁️squishy☁️ Daddy makes me \uD83D\uDCA7wet\uD83D\uDCA7Daddy treats me like his little pet\uD83D\uDC08\uD83D\uDC29\uD83D\uDC15! Send this to 69 \uD83D\uDCAFTRUE\uD83D\uDCAF Daddy's or else you'll \uD83D\uDEABnever\uD83D\uDEAB get any cummies\uD83D\uDCA6\uD83D\uDCA6\uD83D\uDCA6 again \uD83D\uDE26\uD83D\uDE33\uD83D\uDE0E‼️");
        } else if(cmd.equalsIgnoreCase("/4chan")) {
            e.getMessage().updateMessage("Fresh off the boat, from reddit, kid? heh I remember when I was just like you. Braindead. Lemme give you a tip so you can make it in this cyber sanctuary: never make jokes like that. You got no reputation here, you got no name, you got jackshit here. It's survival of the fittest and you ain't gonna survive long on 4chan by saying stupid jokes that your little hugbox cuntsucking reddit friends would upboat. None of that here. You don't upboat. You don't downboat. This ain't reddit, kid. This is 4chan. We have REAL intellectual discussion, something I don't think you're all that familiar with. You don't like it, you can hit the bricks on over to imgur, you daily show watching son of a bitch. I hope you don't tho. I hope you stay here and learn our ways. Things are different here, unlike any other place that the light of internet pop culture reaches. You can be anything here. Me ? heh, I'm a judge.. this place.... this place has a lot to offer... heh you'll see, kid . . . that is if you can handle it.");
        } else if(cmd.equalsIgnoreCase("/help")) {
            StringBuilder sb = new StringBuilder();
            sb.append("```md\n");
            sb.append("/help      | Displays this help message\n");
            sb.append("/lenny     | Pastes lenny\n");
            sb.append("/shrug     | Shrugs\n");
            sb.append("/idle      | Sets IDLE status\n");
            sb.append("/abal      | Mentions abalabahaha\n");
            sb.append("/stop      | Shuts down\n");
            sb.append("/name      | Changes name\n");
            sb.append("/revert    | Reverts name\n");
            sb.append("/purge     | Purgues chat (User and/or amount)\n");
            sb.append("/game      | Changes the game\n");
            sb.append("/thegame   | You just lost\n");
            sb.append("/greentext | Greentext\n");
            sb.append("/gt        | Alias for Greentext\n");
            sb.append("/triforce  | Proves you are not a newfag\n");
            sb.append("/newfag    | Proves you are a newfag\n");
            sb.append("/lmgtfy    | Let Me Google That For You\n");
            sb.append("/tinyurl   | Makes an URL smaller\n");
            sb.append("/navyseal  | Navy seal copypasta\n");
            sb.append("/edgyshit  | Edgy shit copypasta\n");
            sb.append("/goodshit  | Good shit copypasta\n");
            sb.append("/apache    | Attack Helicopter copypasta\n");
            sb.append("/daddy     | Daddy and me copypasta\n");
            sb.append("/4chan     | Found it on a 4chan thread copypasta```");
            e.getMessage().updateMessage(sb.toString());
        }
    }

    public String argsToString(String[] args, int i) {
        StringBuilder sb = new StringBuilder();
        for(int a = i; a < args.length; a++) {
            sb.append(" " + args[a]);
        }
        return sb.toString().replaceFirst(" ", "");
    }

    private static void deleter(List<Message> list, User user) {
        if (list == null || user == null)
            return;
        int index = 0;
        Message target;
        while (index < list.size()) {
            target = list.get(index);
            try {
                if (target.getAuthor().getId().equals(user.getId())) {
                    Message m = target;
                    Thread t = new Thread() {
                        public void run() {
                            m.deleteMessage();
                            Thread.currentThread().interrupt();
                        }
                    };
                    t.start();
                }
            } catch (NullPointerException e) {
                Message m = target;
                Thread t = new Thread() {
                    public void run() {
                        m.deleteMessage();
                        Thread.currentThread().interrupt();
                    }
                };
                t.start();
            }
            index++;
        }
    }


}
