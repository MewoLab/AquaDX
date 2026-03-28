FROM archlinux:latest

RUN pacman -Syu --noconfirm \
    && pacman -S --noconfirm openssh sudo jdk17-openjdk \
    && rm -rf /var/cache/pacman/pkg/* \
    && mkdir -p /var/run/sshd

RUN sed -i 's/.*PermitRootLogin.*/PermitRootLogin yes/' /etc/ssh/sshd_config \
    && sed -i 's/#PermitEmptyPasswords no/PermitEmptyPasswords yes/' /etc/ssh/sshd_config \
    && sed -i 's/#PasswordAuthentication yes/PasswordAuthentication yes/' /etc/ssh/sshd_config \
    && sed -i 's/UsePAM yes/UsePAM no/' /etc/ssh/sshd_config

RUN ssh-keygen -A && passwd -d root

RUN pacman -S --noconfirm zsh git curlie micro ripgrep python3 exa \
    && rm -rf /var/cache/pacman/pkg/* \
    && chsh -s /bin/zsh root \
    && curl -sL hydev.org/zsh | bash

CMD ["/usr/sbin/sshd", "-D"]
