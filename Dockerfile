FROM java:8
COPY dist/ /app/
WORKDIR /app
EXPOSE 8082
ENTRYPOINT ["bin/run.sh"]