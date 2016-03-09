# Add tenant

# --- !Ups

CREATE TABLE IF NOT EXISTS tenant (
  id varchar(255) NOT NULL,
  secret varchar(255) NOT NULL,
  link_api varchar(1000) NOT NULL,
  link_token varchar(1000) NOT NULL,
  PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE tenant;
