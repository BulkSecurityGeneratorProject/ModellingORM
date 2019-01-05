import { NgModule } from '@angular/core';

import { ModellingOrmSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [ModellingOrmSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [ModellingOrmSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ModellingOrmSharedCommonModule {}
