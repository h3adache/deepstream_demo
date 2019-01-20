import { CssBaseline } from '@material-ui/core';
import { createMuiTheme, MuiThemeProvider } from '@material-ui/core/styles';
import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';

const theme = createMuiTheme({
    palette: {
        type: 'dark', // Switching the dark mode on is a single property value change.
    },
    typography: { useNextVariants: true },
});

function DarkTheme() {
    return (
        <MuiThemeProvider theme={theme}>
            <CssBaseline />
            <App/>
        </MuiThemeProvider>
    );
}

ReactDOM.render(<DarkTheme />, document.getElementById('root'));
