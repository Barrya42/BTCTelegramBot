package root;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

public interface MessageHandler
{
    Optional<SendMessage> handleResponse(Message message) throws Throwable;
}
