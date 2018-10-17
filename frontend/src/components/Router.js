import React from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import {ModalFooter} from 'react-bootstrap';

// Components
import Login from './routes/Login';
import Home from './routes/Home.js';
import Achievements from './routes/Achievements';
import Orders from './routes/Orders';
import NewOrder from './routes/CreateOrder';
import Products from './routes/Products';
import NewProduct from './routes/Products/ProductItem';
import Users from './routes/Users';
import Registration from './routes/Registration';
import NotFound from './routes/NotFound';
import QRCode from './routes/QRCode';
import Logout from './routes/Logout';


//import AchievementsLine from './routes/Achievements/AchievementsLine';
import NewAchievement from './routes/Achievements/CreateAchievement/CreateAchievementForm';
//import AchievementDetails from './routes/AchievementDetails';

const Router = () => (
  <BrowserRouter>
    <div>
      <Switch>
        <Route path='/newAchievement' component={NewAchievement} />
        <Route path='/registration' component={Registration} />
        <Route exact path='/' component={Login} />
        <Route path='/achievements' component={Achievements} />
        <Route path='/ordering/:id/newOrder' component={NewOrder} />
        <Route path='/orders' component={Orders} />
        <Route path='/newProduct' component={NewProduct} />
        <Route path='/products' component={Products} />
        <Route path='/users' component={Users} />
        <Route path='/home' component={Home} />
        <Route path='/QRCode' component={QRCode} />
        <Route path='/Logout' component={Logout} />

        <Route component={NotFound} />
      </Switch>
      <ModalFooter>
        <p> PubApp 2018.</p>
      </ModalFooter>
    </div>
  </BrowserRouter>
);
export default Router;
