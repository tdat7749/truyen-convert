package truyenconvert.server.modules.donation.vm;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import truyenconvert.server.modules.book.vm.BookSimpleVm;
import truyenconvert.server.modules.users.vm.UserVm;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class DonationVm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private UserVm user;
    private int coin;
}
