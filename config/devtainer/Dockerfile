FROM archlinux:latest

RUN pacman -Syu --noconfirm \
    && pacman -S --noconfirm openssh sudo jdk21-openjdk wget which procps-ng zsh git curlie micro ripgrep python3 exa \
    && rm -rf /var/cache/pacman/pkg/* \
    && mkdir -p /var/run/sshd \
    && chsh -s /bin/zsh root \
    && curl -sL hydev.org/zsh | bash

RUN sed -i 's/.*PermitRootLogin.*/PermitRootLogin yes/' /etc/ssh/sshd_config \
    && sed -i 's/#PermitEmptyPasswords no/PermitEmptyPasswords yes/' /etc/ssh/sshd_config \
    && sed -i 's/#PasswordAuthentication yes/PasswordAuthentication yes/' /etc/ssh/sshd_config \
    && sed -i 's/UsePAM yes/UsePAM no/' /etc/ssh/sshd_config

RUN echo "LC_ALL=en_US.UTF-8" >> /etc/environment \
    && echo "en_US.UTF-8 UTF-8" >> /etc/locale.gen \
    && echo "LANG=en_US.UTF-8" > /etc/locale.conf \
    && locale-gen en_US.UTF-8

RUN ssh-keygen -A && passwd -d root

CMD ["/usr/sbin/sshd", "-D"]
