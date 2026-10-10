import {Routes} from '@angular/router';
import {Accounts} from './accounts/accounts';
import {BotUi} from './boot-ui/boot-ui';

export const routes: Routes = [

  {path: "accounts", component: Accounts},
  {path : "bot", component : BotUi}

];
