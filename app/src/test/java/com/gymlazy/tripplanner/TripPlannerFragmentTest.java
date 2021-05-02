package com.gymlazy.tripplanner;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class TripPlannerFragmentTest {
    private DatePickerFragment mDatePickerFragment;
    private TripPlannerFragment mSubject;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM d yyyy", Locale.getDefault());
    //Code written inside a method marked @Before will be run once before each test executes
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void checkDate_BeginDateIsCurrent_True() throws ParseException {
        Date dCurrentDate = simpleDateFormat.parse(mSubject.getCurrentDate());
        Date dBeginDate = dCurrentDate;
        assertThat(mSubject.checkDate(mDatePickerFragment.PICK_START_DATE_REQUEST,dCurrentDate,dBeginDate), is(true));
    }

    @Test
    public void checkDate_BeginDateIsPreviousCurrentDate_False() throws ParseException {
        Date dCurrentDate = simpleDateFormat.parse(mSubject.getCurrentDate());
        Date dBeginDate = new Date(dCurrentDate.getTime() - Duration.ofDays(1).toMillis());
        assertThat(mSubject.checkDate(mDatePickerFragment.PICK_START_DATE_REQUEST,dCurrentDate,dBeginDate), is(false));
    }

    @Test
    public void checkDate_BeginDateIsAheadOfEndDate_False() throws ParseException {
        Date dEndDate = simpleDateFormat.parse(mSubject.getCurrentDate());
        Date dBeginDate = new Date(dEndDate.getTime() + Duration.ofDays(1).toMillis());
        assertThat(mSubject.checkDate(mDatePickerFragment.PICK_END_DATE_REQUEST,dBeginDate,dEndDate), is(false));
    }

    @Test
    public void checkNumberTripper_AllowOnlyOneThousandPerKind_True() {
        int iNumPeople = 1000;
        assertThat(mSubject.checkNumberTripper(iNumPeople),is(true));
    }

    @Test
    public void checkRequiredField_ItIsEmpty_False() {
        String sString ="";
        assertThat(mSubject.checkRequiredField(sString),is(false));
    }
}