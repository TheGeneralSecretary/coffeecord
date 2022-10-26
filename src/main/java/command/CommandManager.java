package command;

import command.commands.moderation.*;
import command.commands.superuser.ServerCommand;
import command.commands.superuser.ShutdownCommand;
import command.commands.text.HasteCommand;
import command.commands.text.MD5Command;
import command.commands.utility.HelpCommand;
import command.commands.utility.InfoCommand;
import command.commands.utility.InviteCommand;
import command.commands.utility.PingCommand;
import config.Config;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private static List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
    	// Superuser Commands
		addCommand(new ShutdownCommand());
		addCommand(new ServerCommand());

		// Mod Commands
		addCommand(new KickCommand());
		addCommand(new BanCommand());
		addCommand(new UnbanCommand());
		addCommand(new MuteCommand());
		addCommand(new UnmuteCommand());

        // Utility Commands
        addCommand(new PingCommand());
        addCommand(new HelpCommand());
        addCommand(new InfoCommand());
        addCommand(new InviteCommand());

        // Text Commands
		addCommand(new HasteCommand());
		addCommand(new MD5Command());
    }

    public void addCommand(ICommand command) {
        if(findCommand(command))
            return;

        commands.add(command);
    }

    public static ICommand getCommand(String command) {
        for(ICommand cmd: commands)
            if(cmd.getCommand().equals(command))
                return cmd;

        return null;
    }

    private boolean findCommand(ICommand command) {
        return getCommand(command.getCommand()) != null;
    }

    public static List<ICommand> getCommands() {
    	return commands;
	}

    public void handle(GuildMessageReceivedEvent event) {
        String[] raw = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(Config.get("prefix")), "").split("\\s+");
        String command = raw[0];
        ICommand cmd = getCommand(command);

        if(cmd != null) {
//            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(raw).subList(1, raw.length);
            cmd.execute(new CommandContext(event, args));
        }
    }
}
