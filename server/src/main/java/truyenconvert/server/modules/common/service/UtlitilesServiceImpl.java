package truyenconvert.server.modules.common.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UtlitilesServiceImpl implements UtilitiesService{
    @Override
    public String randomKeyNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i = 1;i<=9;i++){
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return sb.toString();
    }
}
