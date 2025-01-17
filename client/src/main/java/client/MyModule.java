/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.*;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class MyModule implements Module {

    /**
     * Configures bindings between interfaces and their implementations using the given Binder.
     *
     * @param binder the Binder used for configuring bindings
     */
    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(EventOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddEditParticipantsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddEditExpenseCtrl.class).in(Scopes.SINGLETON);
        binder.bind(OpenDebtsCtrl.class).in(Scopes.SINGLETON);

        // should we configure the other classes here as well?
    }
}