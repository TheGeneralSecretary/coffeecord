package command.commands.utility;

import command.CommandContext;
import command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

public class PingCommand implements ICommand {
    @Override
    public void execute(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        channel.sendMessage("PONG! `" + ctx.getJDA().getGatewayPing() + "ms`").queue();
    }

    @Override
    public String getCommand() {
        return "ping";
    }

    @Override
    public String getHelp() {
        return "Get Gateway Ping";
    }
}
