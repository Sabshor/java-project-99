FROM gradle:8.7.0-jdk21

WORKDIR /app

COPY / .

RUN gradle installBootDist

CMD ./build/install/app-boot/bin/app
