FROM gradle:8.8.0-jdk21

ENV NODE_VERSION=22

RUN apt-get update && \
    apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_${NODE_VERSION}.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g npm@latest

RUN npm install -g bun

RUN apt-get install -y maven

RUN gradle --version && \
    node --version && \
    npm --version && \
    bun --version

WORKDIR /workspace