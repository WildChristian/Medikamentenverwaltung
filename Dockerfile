FROM airhacks/glassfish
COPY ./target/mrv.war ${DEPLOYMENT_DIR}
