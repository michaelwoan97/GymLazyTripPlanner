/*
*	PROJECT: Trip Planner
*	FILE: HotelDbSchema.java
*	PROGRAMMER: Nghia Nguyen
*	FIRST VERSION: 2021/05/06
*	DESCRIPTION:
		This file contains the HotelDbSchema class defined the database schema in constants
*/

package com.gymlazy.tripplanner.Model.databases;

public class HotelDbSchema {
    public static final class hotelTable{
        public static final String NAME = "hotels";
    }
    public static final class Cols{
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String IMG = "imgURL";
        public static final String FAVORITE = "favorite";
        public static final String DESCRIPTION = "description";
        public static final String ADDRESS = "address";
        public static final String WEB_URL = "webURL";
        public static final String PHONE_NUMBER = "phoneNumber";
    }
}
