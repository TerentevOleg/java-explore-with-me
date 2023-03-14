CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) UNIQUE,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat FLOAT4,
    lon FLOAT4,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation VARCHAR(2000),
    category_id BIGINT references categories (id),
    created_on VARCHAR(255),
    description VARCHAR(7000),
    event_date TIMESTAMP,
    initiator_id BIGINT references users (id),
    location_id BIGINT references locations (id),
    paid BOOLEAN NOT NULL,
    participant_limit INT NOT NULL,
    published_on VARCHAR(255),
    state VARCHAR,
    request_moderation BOOLEAN NOT NULL,
    title VARCHAR(255),
    views BIGINT,
    CONSTRAINT pk_event PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP,
    event BIGINT references events (id),
    requester BIGINT references users (id),
    status VARCHAR(255),
    CONSTRAINT pk_requests PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN,
    title VARCHAR(255),
    CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilation_event (
    compilation_id BIGINT REFERENCES compilations(id),
    event_id BIGINT REFERENCES events(id),
    CONSTRAINT ce_pk PRIMARY KEY (compilation_id, event_id)
);