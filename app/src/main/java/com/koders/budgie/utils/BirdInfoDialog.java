package com.koders.budgie.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.koders.budgie.R;
import com.koders.budgie.model.BirdInfo;

public class BirdInfoDialog {

    private Activity activity;
    AlertDialog dialog;
    TextView ringNum, sex, hatchDate, arrivalDate, approxAge, size, color, crested, father, mother, status, cageNumber, ringOwnerName, purchasedPrice, takenFrom, takenDate, sellerNumber,
            sellerLocation, sellingPrice, givenTo, givenDate, buyerNumber, buyerLocation, withPartnership, mutation;

    public BirdInfoDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(BirdInfo birdInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.bird_info_dialog, null);

        ringNum = view.findViewById(R.id.ringNumber);
        sex = view.findViewById(R.id.sex);
        hatchDate = view.findViewById(R.id.hatchDate);
        arrivalDate = view.findViewById(R.id.arrivalDate);
        approxAge = view.findViewById(R.id.approxAge);
        size = view.findViewById(R.id.size);
        color = view.findViewById(R.id.color);
        size = view.findViewById(R.id.size);
        crested = view.findViewById(R.id.crested);
        father = view.findViewById(R.id.father);
        mother = view.findViewById(R.id.mother);
        status = view.findViewById(R.id.status);
        cageNumber = view.findViewById(R.id.cageNumber);
        ringOwnerName = view.findViewById(R.id.ringOwnerName);
        purchasedPrice = view.findViewById(R.id.purchasedPrice);
        takenFrom = view.findViewById(R.id.takenFrom);
        purchasedPrice = view.findViewById(R.id.purchasedPrice);
        takenDate = view.findViewById(R.id.takenDate);
        purchasedPrice = view.findViewById(R.id.purchasedPrice);
        sellerNumber = view.findViewById(R.id.sellerNumber);
        sellerLocation = view.findViewById(R.id.sellerLocation);
        sellingPrice = view.findViewById(R.id.sellingPrice);
        givenTo = view.findViewById(R.id.givenTo);
        givenDate = view.findViewById(R.id.givenDate);
        buyerNumber = view.findViewById(R.id.buyerNumber);
        buyerLocation = view.findViewById(R.id.buyerLocation);
        withPartnership = view.findViewById(R.id.withPartnership);
        mutation = view.findViewById(R.id.mutation);

        if (birdInfo != null) {
            if (birdInfo.getRingNumber() == null || birdInfo.getRingNumber().equals("")) {
                ringNum.setText("\t----");
            } else {
                ringNum.setText(birdInfo.getRingNumber());
            }
            if (birdInfo.getSex() == null || birdInfo.getSex().equals("")) {
                sex.setText("\t----");
            } else {
                sex.setText(birdInfo.getSex());
            }
            if (birdInfo.getHatchDate() == null || birdInfo.getHatchDate().equals("")) {
                hatchDate.setText("\t----");
            } else {
                hatchDate.setText(birdInfo.getHatchDate());
            }
            if (birdInfo.getArrivalDate() == null || birdInfo.getArrivalDate().equals("")) {
                arrivalDate.setText("\t----");
            } else {
                arrivalDate.setText(birdInfo.getArrivalDate());
            }
            if (birdInfo.getApproxAge() == null || birdInfo.getApproxAge().equals("")) {
                approxAge.setText("\t----");
            } else {
                approxAge.setText(birdInfo.getApproxAge());
            }
            if (birdInfo.getSize() == null || birdInfo.getSize().equals("")) {
                size.setText("\t----");
            } else {
                size.setText(birdInfo.getSize());
            }
            if (birdInfo.getColor() == null || birdInfo.getColor().equals("")) {
                color.setText("\t----");
            } else {
                color.setText(birdInfo.getColor());
            }
            if (birdInfo.getCrested() == null || birdInfo.getCrested().equals("")) {
                crested.setText("\t----");
            } else {
                crested.setText(birdInfo.getSex());
            }
            if (birdInfo.getFather() == null || birdInfo.getFather().equals("")) {
                father.setText("\t----");
            } else {
                father.setText(birdInfo.getFather());
            }
            if (birdInfo.getMother() == null || birdInfo.getMother().equals("")) {
                mother.setText("\t----");
            } else {
                mother.setText(birdInfo.getMother());
            }
            if (birdInfo.getStatus() == null || birdInfo.getStatus().equals("")) {
                status.setText("\t----");
            } else {
                status.setText(birdInfo.getStatus());
            }
            if (birdInfo.getCageNumber() == null || birdInfo.getCageNumber().equals("")) {
                cageNumber.setText("\t----");
            } else {
                cageNumber.setText(birdInfo.getCageNumber());
            }
            if (birdInfo.getRingOwnerName() == null || birdInfo.getRingOwnerName().equals("")) {
                ringOwnerName.setText("\t----");
            } else {
                ringOwnerName.setText(birdInfo.getRingOwnerName());
            }
            if (birdInfo.getPurchasedPrice() == null || birdInfo.getPurchasedPrice().equals("")) {
                purchasedPrice.setText("\t----");
            } else {
                purchasedPrice.setText(birdInfo.getPurchasedPrice());
            }
            if (birdInfo.getTakenFrom() == null || birdInfo.getTakenFrom().equals("")) {
                takenFrom.setText("\t----");
            } else {
                takenFrom.setText(birdInfo.getSex());
            }
            if (birdInfo.getTakenDate() == null || birdInfo.getTakenDate().equals("")) {
                takenDate.setText("\t----");
            } else {
                takenDate.setText(birdInfo.getTakenDate());
            }
            if (birdInfo.getSellerNumber() == null || birdInfo.getSellerNumber().equals("")) {
                sellerNumber.setText("\t----");
            } else {
                sellerNumber.setText(birdInfo.getSellerNumber());
            }
            if (birdInfo.getSellerLocation() == null || birdInfo.getSellerLocation().equals("")) {
                sellerLocation.setText("\t----");
            } else {
                sellerLocation.setText(birdInfo.getSellerLocation());
            }
            if (birdInfo.getSellingPrice() == null || birdInfo.getSellingPrice().equals("")) {
                sellingPrice.setText("\t----");
            } else {
                sellingPrice.setText(birdInfo.getSellingPrice());
            }
            if (birdInfo.getGivenTo() == null || birdInfo.getGivenTo().equals("")) {
                givenTo.setText("\t----");
            } else {
                givenTo.setText(birdInfo.getGivenTo());
            }
            if (birdInfo.getGivenDate() == null || birdInfo.getGivenDate().equals("")) {
                givenDate.setText("\t----");
            } else {
                givenDate.setText(birdInfo.getGivenDate());
            }
            if (birdInfo.getBuyerNumber() == null || birdInfo.getBuyerNumber().equals("")) {
                buyerNumber.setText("\t----");
            } else {
                buyerNumber.setText(birdInfo.getBuyerNumber());
            }
            if (birdInfo.getBuyerLocation() == null || birdInfo.getBuyerLocation().equals("")) {
                buyerLocation.setText("\t----");
            } else {
                buyerLocation.setText(birdInfo.getBuyerLocation());
            }
            if (birdInfo.getWithPartnership() == null || birdInfo.getWithPartnership().equals("")) {
                withPartnership.setText("\t----");
            } else {
                withPartnership.setText(birdInfo.getWithPartnership());
            }
            if (birdInfo.getMutation() == null || birdInfo.getMutation().equals("")) {
                mutation.setText("\t----");
            } else {
                mutation.setText(birdInfo.getMutation());
            }
        }

        builder.setTitle("Bird Info : ");
        builder.setView(view);
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

}
