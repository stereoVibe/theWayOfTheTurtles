package io.sokolvault13.turtlesway.presenters.SubGoalsList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import io.sokolvault13.turtlesway.R;
import io.sokolvault13.turtlesway.db.DatabaseHelper;
import io.sokolvault13.turtlesway.db.HelperFactory;
import io.sokolvault13.turtlesway.model.BigGoal;
import io.sokolvault13.turtlesway.model.IntentionDAOHelper;
import io.sokolvault13.turtlesway.presenters.BigGoalsList.BigGoalsListActivity;
import io.sokolvault13.turtlesway.utils.Constants;

public class DeleteBigGoalConfirmationDialog extends DialogFragment {
    DatabaseHelper helper;
    BigGoal bigGoal;
    int bigGoalID;
    Dao<BigGoal, Integer> bigGoalDAO;

    public DeleteBigGoalConfirmationDialog() {
    }

    /*
     * Creating instance of the dialog with id of the deleting goal
     */
    public static DeleteBigGoalConfirmationDialog newInstance(String title, int bigGoalID) {
        DeleteBigGoalConfirmationDialog dialog = new DeleteBigGoalConfirmationDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt(Constants.EXTRA_BIG_GOAL_ID, bigGoalID);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        helper = HelperFactory.getHelper();
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        HelperFactory.releaseHelper();
        super.onDestroyView();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title"),
                confirmation = getResources().getString(R.string.delete_big_goal_dialog),
                ok = getResources().getString(R.string.confirm_delete_big_goal_dialog),
                cancel = getResources().getString(R.string.cancel_delete_big_goal_dialog);
        bigGoalID = getArguments().getInt(Constants.EXTRA_BIG_GOAL_ID);

        new initializeBigGoal().execute(helper);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(title);
        alertDialog.setMessage(confirmation);

        alertDialog.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    /* Accessing the database and the implementation of the removal*/
                    IntentionDAOHelper.deleteIntention(bigGoal, bigGoalDAO);
                } catch (SQLException e) {
                    Log.e(Constants.DAO_ERROR, "Removal of the Big Goal can't be done.");
                    e.printStackTrace();
                }
                /*
                * Return to the entry screen with list of Big Goals
                */
                Intent intent = new Intent(getContext(), BigGoalsListActivity.class);
                startActivity(intent);
                dismiss();
            }
        });

        alertDialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return alertDialog.create();
    }

    private class initializeBigGoal extends AsyncTask<DatabaseHelper, Integer, DatabaseHelper> {

        @Override
        protected void onPreExecute() {
            HelperFactory.setHelper(getContext());
            super.onPreExecute();
        }

        @Override
        protected DatabaseHelper doInBackground(DatabaseHelper... databaseHelpers) {
            helper = HelperFactory.getHelper();
            return helper;
        }

        @Override
        protected void onPostExecute(DatabaseHelper databaseHelper) {
            try {
                bigGoalDAO = databaseHelper.getBigGoalDAO();
                bigGoal = IntentionDAOHelper.getBigGoal(bigGoalDAO, bigGoalID);
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e(Constants.DAO_ERROR, "Can't get bigGoal object.");
            }
        }
    }
}