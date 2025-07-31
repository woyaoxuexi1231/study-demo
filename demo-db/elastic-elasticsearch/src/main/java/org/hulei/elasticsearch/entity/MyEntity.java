package org.hulei.elasticsearch.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.Date;

@Document(indexName = "my_entity_index", createIndex = true)
public class MyEntity {

    @Id
    private String id;

    @JoinTypeRelations(
        relations = {@JoinTypeRelation(parent = "group", children = {"event"})}
    )
    private String relationshipType;

    private String organizer;

    private String name;

    @Field(type = FieldType.Text, termVector = TermVector.with_positions_offsets)
    private String description;

    @Field(type = FieldType.Date, format = DateFormat.date, pattern = "yyyy-MM-dd")
    private Date createdOn;

    @MultiField(
        mainField = @Field(type = FieldType.Text),
        otherFields = {@InnerField(suffix = "verbatim", type = FieldType.Keyword)}
    )
    private String tags;

    private String members;

    private String locationGroup;

    private String host;

    private String title;

    @MultiField(
        mainField = @Field(type = FieldType.Text),
        otherFields = {@InnerField(suffix = "verbatim", type = FieldType.Keyword)}
    )
    private String attendees;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute)
    private Date date;

    @Field(type = FieldType.Integer, nullValue = "0")
    private Integer reviews;

    @Field(type = FieldType.Nested)
    private LocationEvent locationEvent;

    // Nested class for location_event
    public static class LocationEvent {

        private String name;

        @GeoPointField
        private GeoPoint geolocation;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public GeoPoint getGeolocation() {
            return geolocation;
        }

        public void setGeolocation(GeoPoint geolocation) {
            this.geolocation = geolocation;
        }
    }

    // Getters and Setters for main class
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getLocationGroup() {
        return locationGroup;
    }

    public void setLocationGroup(String locationGroup) {
        this.locationGroup = locationGroup;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAttendees() {
        return attendees;
    }

    public void setAttendees(String attendees) {
        this.attendees = attendees;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getReviews() {
        return reviews;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public LocationEvent getLocationEvent() {
        return locationEvent;
    }

    public void setLocationEvent(LocationEvent locationEvent) {
        this.locationEvent = locationEvent;
    }
}
