import { GridReadyEvent } from "ag-grid-community";
import "ag-grid-community/dist/styles/ag-grid.css";
import "ag-grid-community/dist/styles/ag-theme-dark.css";
import { AgGridReact } from "ag-grid-react";
import React from "react";

interface Props {
  trades: [];
}

function TradesList(props: Props) {
  return (
    <div className="ag-theme-dark" style={{height:700}}>
      <AgGridReact
        onGridReady={(params: GridReadyEvent) =>
          params.api && params.api.sizeColumnsToFit()
        }
        columnDefs={[
          {
            headerName: "Product Id",
            valueGetter: (param: any) => param.data[0]
          },
          { headerName: "Side", valueGetter: (param: any) => param.data[1] },
          { headerName: "Price", valueGetter: (param: any) => param.data[2] },
          { headerName: "Quantity", valueGetter: (param: any) => param.data[3] }
        ]}
        rowData={props.trades}
      />
    </div>
  );
}

export default TradesList;
