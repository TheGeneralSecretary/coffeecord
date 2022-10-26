package command.commands.utility;

import coffeecord.Util;
import command.CommandContext;
import command.CommandManager;
import command.ICommand;
import config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class HelpCommand implements ICommand {
    private static String botInviteUrl;

    @Override
    public void execute(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        botInviteUrl = ctx.getJDA().getInviteUrl(Permission.ADMINISTRATOR);

        if(args.size() > 0) {
            ICommand cmd = CommandManager.getCommand(args.get(0));
            if(cmd != null) {
                channel.sendMessage(cmd.getHelp()).queue();
                return;
            }
        }

        channel.sendMessageEmbeds(getHelpEmbed().build()).queue();
    }

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "help menu\n" + "help [COMMAND]";
    }

    private EmbedBuilder getHelpEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Help Commands");
        embed.setColor(Color.CYAN);
        embed.setDescription("Prefix: " + Config.get("prefix") + "\nVersion: 0.0.1");

        embed.addField("\u276F Home Server", "[CoffeeCord](#link)", true);
        embed.addField("\u276F Invite Bot", "[CoffeeCord](" + botInviteUrl + ")", true);
        embed.addField("\u276F Author", "[TheGeneralSecretary](https://github.com/TheGeneralSecretary)", true);
        embed.getFields().addAll(getCommandsEmbed().getFields());

        embed.setFooter("\u276F \u00A9 2022 TheGeneralSecretary");
        return embed;
    }

	private EmbedBuilder getCommandsEmbed() {
		EmbedBuilder embed = new EmbedBuilder();
		HashMap<String, List<ICommand>> commands = Util.getCurrentPackageHashmap(CommandManager.getCommands());
		StringBuilder fields = new StringBuilder();

		for(var entry: commands.entrySet()) {
			String section = entry.getKey();
			for(var value: entry.getValue()) {
				var command = value.getCommand();
				fields.append("`" + command + "` ");
			}
			embed.addField("\u276F " + Util.capitalize(section), fields.toString(), true);
			fields.setLength(0);
		}

		return embed;
	}
}
