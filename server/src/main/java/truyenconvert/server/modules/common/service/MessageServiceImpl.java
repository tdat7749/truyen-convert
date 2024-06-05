package truyenconvert.server.modules.common.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService{

    private final MessageSource messageSource;

    public MessageServiceImpl(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String name) {
        return messageSource.getMessage(name, null, LocaleContextHolder.getLocale());
    }
}
