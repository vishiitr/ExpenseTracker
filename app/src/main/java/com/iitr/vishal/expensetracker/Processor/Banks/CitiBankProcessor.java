package com.iitr.vishal.expensetracker.Processor.Banks;

import com.iitr.vishal.expensetracker.Common.Constants;
import com.iitr.vishal.expensetracker.Model.SmsModel;
import com.iitr.vishal.expensetracker.Model.TranscationModel;
import com.iitr.vishal.expensetracker.Processor.BankProcessor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Divya on 17-03-2018.
 */

public class CitiBankProcessor implements BankProcessor.IBankProcessor {
    private final String spendingRegex = "(Rs\\.|Rs\\s|INR\\s)?([\\d,\\.]*).*spent.*XXX(\\d+)\\son\\s(.{9,11})\\sat\\s([a-zA-Z0-9\\s]+\\.?).*";

    @Override
    public TranscationModel onSaveTranscation(SmsModel smsModel) {
        Pattern p = Pattern.compile(spendingRegex);
        Matcher m = p.matcher(smsModel.getMsg());

        if (m.matches()) {
            float spentAmount = Float.parseFloat(m.group(2).replaceAll(",", ""));
            String spendingCard = m.group(3);
            String spentDate = m.group(4);
            String spentAt = m.group(5);

            //long bankId = saveBank(Constants.BANKNAMECITI, spendingCard);
            //saveTranscation(smsModel.getId(), spentAmount, spentDate, spentAt, bankId);

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAt = spentAt;
            transcationModel.spentAmount = spentAmount;
            transcationModel.spendingCard = spentAt;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMECITI;
            transcationModel.spentDate = convertToDate(spentDate);
            return transcationModel;
        }
        return null;
    }


    private Date convertToDate(String spentDate) {
        DateFormat sourceFormat = new SimpleDateFormat("dd-MMM-yy");
        Date date = null;
        try {
            date = sourceFormat.parse(spentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
        //BankProcessor.saveTranscation(Integer.parseInt(id), spentAmount, date, spentAt, bankId);
    }
}
