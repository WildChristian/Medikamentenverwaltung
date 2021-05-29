# Build
mvn clean package && docker build -t  de.hsb.app/mrv .

# RUN

docker rm -f mrv || true && docker run -d -p 8080:8080 -p 4848:4848 --name mrv  de.hsb.app/mrv 