package com.example.riova;

/**
 * This is a single ambient sound object that a user can play during a meditation
 * session. Each sound has a name, a short description, and the resource ID
 */
public class Sound {

    private final String name;
    private final String description;
    private final int resourceId;
    private boolean favorite;

    /**
     * creates a new sound
     *
     * @param name          name of the sound
     * @param description   a short description
     * @param resourceId    resource ID of the audio file
     */
    public Sound(String name, String description, int resourceId) {
        this.name = name;
        this.description = description;
        this.resourceId = resourceId;
        this.favorite = false;
    }

    /**
     * returns name of the sound.
     *
     * @return name of the sound
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the short description
     *
     * @return sound's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the resource ID of the sound
     *
     * @return the audio resource ID
     */
    public int getResourceId() {
        return resourceId;
    }

    /**
     * returns whether the user has marked this sound as a favorite or not
     *
     * @return {@code true} if the sound is set as favorite, or {@code false} if false
     */
    public boolean isFavorite() {
        return favorite;
    }

    /**
     * marks or unmarks sound as favorite
     *
     * @param favorite {@code true} mark favorite, {@code false} unmark favorite
     */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return name;
    }
}