package by.bsuir.mobileplatforms2.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import by.bsuir.mobileplatforms2.R;
import by.bsuir.mobileplatforms2.entity.History;
import by.bsuir.mobileplatforms2.service.HistoryService;
import by.bsuir.mobileplatforms2.service.impl.HistoryServiceImpl;

public class HistoryFragment extends Fragment {

    public HistoryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Заполнить таблицу
        HistoryService historyService = new HistoryServiceImpl(getContext());
        List<History> histories = historyService.getAllHistory();
        TableLayout tableLayout = view.findViewById(R.id.history_tableLayout);
        for (History history : histories) {
            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow_history_table, null);
            ((TextView) tableRow.findViewById(R.id.tablerow_history_table_column1)).setText(history.getUser().getUsername());
            ((TextView) tableRow.findViewById(R.id.tablerow_history_table_column2)).setText(history.getCategory());
            ((TextView) tableRow.findViewById(R.id.tablerow_history_table_column3)).setText(history.getPrice().toString());
            ((TextView) tableRow.findViewById(R.id.tablerow_history_table_column4)).setText(history.getCreatedAt().toString());
            tableLayout.addView(tableRow);
        }
    }
}