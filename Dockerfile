FROM gradle:8.12.1-jdk21

WORKDIR /app

COPY /app .

# #команды для сборки в github pipeline
RUN ["./gradlew", "clean", "build"]
CMD ["./gradlew", "run"]

# #команды для запуска в railway
# RUN gradle installDist
# CMD ./build/install/app/bin/app
